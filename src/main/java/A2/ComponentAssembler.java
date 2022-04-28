package A2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.IntStream;
import java.util.stream.Stream;

enum Breaker {
    BACK, EXIT, NONE
}

public class ComponentAssembler implements Runnable {

    @SuppressWarnings("FieldCanBeLocal")
    private final String pathResources = "src/main/java/A2/resources/";
    private final Scanner scanner;

    Map<Thread, Component> components;

    public ComponentAssembler() {
        this.scanner = new Scanner(System.in);
        this.components = new HashMap<>();
    }

    @Override
    public void run() {
        System.out.println("Start Component Assembler");
        while (true) {
            System.out.println(Helper.getLine());
            String[] options = {"show status", "load component", "unload component", "start component", "stop component"};
            int input = ask(Helper.GREEN + "Please select number:" + Helper.ANSI_RESET, options, Breaker.EXIT);
            if (input == 0) { // show status
                printStatus();
            } else if (input == 1) { // load component
                try {
                    loadComponent();
                } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 2) { // unload component
                unloadComponent();
            } else if (input == 3) { // start component
                System.out.println("Start Component");
            } else if (input == 4) { // stop component
                System.out.println("Stop Component");
            } else if (input >= options.length) { // exit
                System.out.println("Goodbye!");
                break;
            }
        }
    }

/*    static Object neuesExemplar(String pfad, String klassename) throws Exception {
        URL url = new File(pfad).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class<?> c = cl.loadClass(klassename);
        // Method[] methods = c.getMethods();

        Method method = c.getDeclaredMethod("main", String[].class);

        String[] args = {"arg1", "arg2"};
        System.out.println("A");

        Object instance = c.newInstance();
        System.out.println("B");
        Object result = method.invoke(null, (Object) args);

        return c.newInstance();
    }*/

    /*    static <T> T[] append(T[] arr, T element) {
            final int n = arr.length;
            arr = Arrays.copyOf(arr, n + 1);
            arr[n] = element;
            return arr;
        }*/
    private void printStatus() {
        System.out.println(Helper.getLine());
        System.out.println("Status");
        System.out.println("Running threads: " + components.entrySet().size());
        int counter = 0;
        for (Map.Entry<Thread, Component> set : components.entrySet()) {
            System.out.println(Helper.getLine());
            System.out.println("Thread #" + counter++);
            System.out.println(set.getValue());
        }
    }

    private void loadComponent() throws
            IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {

        // SELECT COMPONENT
        String[] components = getJarFiles(pathResources);
        int selectedComponent = ask("Please select a component:", components, Breaker.BACK);
        if (selectedComponent >= components.length) return;

        // SELECT CLASS
        String dirComponent = pathResources + components[selectedComponent];
        String[] classes = getClassNamesFromJarFile(dirComponent);
        int selectedClass = ask("Please select a class:", classes, Breaker.BACK);
        if (selectedClass >= classes.length) return;

        // SELECT METHOD
        String[] methods = getMethods(classes, selectedClass, dirComponent);
        int selectedMethod = ask("Please select method:", methods, Breaker.BACK);
        if (selectedMethod >= methods.length) return;

//        runMethod(classes, selectedClass, dirComponent, selectedMethod);

        // CREATE THREAD
        Component component = new Component(pathResources, components[selectedComponent], classes[selectedClass], methods[selectedMethod]);
        Thread thread = new Thread(component);
        this.components.put(thread, component);
        System.out.println(Helper.getLine());
        System.out.println("A new thread has been created.");
        System.out.println(component);

        // RUN THREAD?
        if (askYesNo("Start thread?")) {
            thread.start();
        }
    }

    private void unloadComponent() {
        List<String> optionsList = new ArrayList<>();
        components.forEach((key, component) -> optionsList.add("unload " + component.getId() + "#" + component.getSelectedComponent()));
        String[] options = optionsList.toArray(String[]::new);
        int unload = ask("Select to unload: ", options, Breaker.BACK);
        if (unload >= options.length) {
            return;
        }
        Thread[] threads = components.keySet().toArray(Thread[]::new);
        Thread thread = threads[unload];
        thread.interrupt();
        components.remove(thread);
    }

    private String[] getJarFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles())).filter(file -> !file.isDirectory() && file.getName().endsWith(".jar")).map(File::getName).toArray(String[]::new);
    }

    private String[] getClassNamesFromJarFile(String dir) throws IOException {
        Set<String> set = new HashSet<>();
        JarFile jarFile = new JarFile(dir);
        Enumeration<JarEntry> e = jarFile.entries();
        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            String className = je.getName().substring(0, je.getName().length() - 6);
            set.add(className.replace('/', '.'));
        }
        return set.toArray(new String[0]);
    }

    private String[] getMethods(String[] classes, int selectedClass, String dir) throws
            MalformedURLException, ClassNotFoundException {
        URL url = new File(dir).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class<?> c = cl.loadClass(classes[selectedClass]);
        Method[] methods = c.getDeclaredMethods();
        return Arrays.stream(methods).map(Method::getName).toArray(String[]::new);
    }

    private void runMethod(String[] classes, int selectedClass, String pathComponent, int selectedMethod) throws
            MalformedURLException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        URL url = new File(pathComponent).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class<?> c = cl.loadClass(classes[selectedClass]);
        Method method = c.getDeclaredMethods()[selectedMethod];
        String[] args = {};
        // Object instance = c.newInstance();
        // Object result =
        method.invoke(null, (Object) args);
    }

    private int ask(String question, String[] options, Breaker breaker) {
        System.out.println(question);
        IntStream.range(0, options.length).mapToObj(i -> "[" + i + "] " + options[i]).forEach(System.out::println);
        if (breaker == Breaker.BACK || breaker == Breaker.EXIT) {
            System.out.println("[" + options.length + "] " + breaker.toString().toLowerCase(Locale.ROOT));
        }
        int input = scanner.nextInt();
        if (input < 0 || (breaker == Breaker.NONE && input >= options.length) || input >= options.length + 1) {
            System.out.println("Wrong input.");
            return ask(question, options, breaker);
        }
        return input;
    }

    private boolean askYesNo(String question) {
        return ask("Start thread?", new String[]{"yes", "no"}, Breaker.NONE) == 0;
    }
}
