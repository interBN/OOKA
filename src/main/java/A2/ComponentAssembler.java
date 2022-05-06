package A2;

import A2.annotations.StartClassDeclaration;
import A2.annotations.StartMethodDeclaration;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static A2.Helper.Breaker.*;

public class ComponentAssembler implements Runnable {

    private static ComponentAssembler singleton = null;
    @SuppressWarnings("FieldCanBeLocal")
    private final String pathResources = "src/main/java/A2/resources/";
    private final Scanner scanner;
    Map<Thread, Component> components;

    private ComponentAssembler() {
        this.scanner = new Scanner(System.in);
        this.components = new HashMap<>();
    }

    public static ComponentAssembler getInstance() {
        if (singleton == null) {
            singleton = new ComponentAssembler();
        }
        return singleton;
    }

    @Override
    public void run() {
        System.out.println("Start Component Assembler");
        while (true) {
            System.out.println(Helper.getLine());
            String[] options = {"show status", "load component", "unload component", "start component", "stop component"};
            int input = ask("Please select:", options, EXIT);
            if (input == 0) { // show status
                printStatus();
            } else if (input == 1) { // load component
                try {
                    loadComponent();
                } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException |
                         InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 2) { // unload component
                unloadComponent();
            } else if (input == 3) { // start component
                System.out.println("Start Component");
                try {
                    startComponent();
                } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                         NoSuchMethodException | MalformedURLException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 4) { // stop component
                try {
                    stopComponent();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (input >= options.length) { // exit
                System.out.println("Goodbye!");
                break;
            }
        }
    }

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

    private void loadComponent() throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InterruptedException {

        // SELECT COMPONENT
        String[] components = getJarFiles(pathResources);
        int selectedComponent = ask("Please select a component:", components, BACK);
        if (selectedComponent >= components.length) return;

        // SELECT CLASS
        String dirComponent = pathResources + components[selectedComponent];
        String[] classes = getClassNamesFromJarFile(dirComponent);
        int selectedClass = ask("Please select a class:", classes, BACK);
        if (selectedClass >= classes.length) return;

        // SELECT METHOD
        String[] methods = getMethods(classes[selectedClass], dirComponent);
        int selectedMethod = ask("Please select a method:", methods, BACK);
        if (selectedMethod >= methods.length) return;

        // CREATE THREAD
        Component component = new Component(pathResources, components[selectedComponent], classes[selectedClass], methods[selectedMethod], selectedMethod);
        Thread thread = new Thread(component);
        this.components.put(thread, component);
        System.out.println(Helper.getLine());
        System.out.println("A new thread has been created.");
        System.out.println(component);

        // RUN THREAD?
        if (askYesNo("Start thread?")) {
            thread.start();
            thread.join();
        }
    }

    private void unloadComponent() {
        String[] options = listAllThreads("unload ");
        int unload = ask("Select to unload: ", options, BACK);
        if (unload >= options.length) {
            return;
        }
        Component[] comps = components.values().toArray(Component[]::new);
        Component component = comps[unload];
        component.close();
        Thread[] threads = components.keySet().toArray(Thread[]::new);
        Thread thread = threads[unload];
        thread.interrupt();
        components.remove(thread);
    }

    private void startComponent() throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedURLException, InterruptedException {
        String[] options = listAllThreads("start ");
        int start = ask("Select to start: ", options, BACK);
        if (start >= options.length) {
            return;
        }
        /*
          The old thread can be in state TERMINATED.
          Therefore, we need a new thread to avoid exceptions.
          The component can be reused.
         */
        Component[] components = this.components.values().toArray(Component[]::new);
        Component component = components[start];
        Thread[] threads = this.components.keySet().toArray(Thread[]::new);
        Thread oldThread = threads[start];
        this.components.remove(oldThread);
        Thread newThread = new Thread(component);
        this.components.put(newThread, component);
        newThread.start();
        newThread.join();
    }

    private void stopComponent() throws IOException {
        String[] options = listAllThreads("stop ");
        int start = ask("Select to stop: ", options, BACK);
        if (start >= options.length) {
            return;
        }
        Component[] components = this.components.values().toArray(Component[]::new);
        Component thread = components[start];
        thread.stopInit();
    }

    private String[] getJarFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles())).filter(file -> !file.isDirectory() && file.getName().endsWith(".jar")).map(File::getName).toArray(String[]::new);
    }

    private String[] listAllThreads(String prefix) {
        List<String> optionsList = new ArrayList<>();
        components.forEach((key, component) -> {
            String line = prefix
                    + Helper.BLUE + component.getId() + Helper.ANSI_RESET
                    + "#"
                    + Helper.YELLOW + component.getSelectedComponent() + Helper.ANSI_RESET
                    + "#"
                    + Helper.RED + component.isActive() + Helper.ANSI_RESET;
            optionsList.add(line);
        });
        return optionsList.toArray(String[]::new);
    }

    /**
     * source: <a href="https://stackoverflow.com/a/11016392">https://stackoverflow.com/a/11016392</a>
     */
    private String[] getClassNamesFromJarFile(String dir) throws IOException, ClassNotFoundException {
        Set<String> set = new HashSet<>();
        JarFile jarFile = new JarFile(dir);
        Enumeration<JarEntry> e = jarFile.entries();

        URL[] urls = {new URL("jar:file:" + dir + "!/")};
        URLClassLoader cl = URLClassLoader.newInstance(urls);

        while (e.hasMoreElements()) {
            JarEntry je = e.nextElement();
            if (je.isDirectory() || !je.getName().endsWith(".class")) {
                continue;
            }
            String className = je.getName().substring(0, je.getName().length() - 6);
            Class<?> c = cl.loadClass(className);
            StartClassDeclaration annotation = c.getAnnotation(StartClassDeclaration.class);
            if (annotation == null) {
                continue;
            }
//            String className = je.getName().substring(0, je.getName().length() - 6);
            set.add(className.replace('/', '.'));
        }
        return set.toArray(new String[0]);
    }

    private String[] getMethods(String classArg, String dir) throws MalformedURLException, ClassNotFoundException {
        URL url = new File(dir).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class<?> c = cl.loadClass(classArg);
        Method[] methods = c.getDeclaredMethods();
        List<String> methodsFiltered = new ArrayList<>();
        for (Method method : methods) {
            StartMethodDeclaration annotation2 = method.getDeclaredAnnotation(StartMethodDeclaration.class);
            if (annotation2 != null) {
                methodsFiltered.add(method.getName());
            }
        }
//        c.getMethod("main", String[].class)
        return methodsFiltered.toArray(String[]::new);
    }

    private int ask(String question, String[] options, Helper.Breaker breaker) {
        System.out.println(Helper.GREEN + question + Helper.ANSI_RESET);
        IntStream.range(0, options.length).mapToObj(i -> "[" + i + "] " + options[i]).forEach(System.out::println);
        if (breaker == BACK || breaker == EXIT) {
            System.out.println("[" + options.length + "] " + breaker.toString().toLowerCase(Locale.ROOT));
        }
        int input = scanner.nextInt();
        if (input < 0 || (breaker == NONE && input >= options.length) || input >= options.length + 1) {
            System.out.println(Helper.RED + "Wrong input." + Helper.ANSI_RESET);
            return ask(question, options, breaker);
        }
        return input;
    }

    private boolean askYesNo(String question) {
        return ask(question, new String[]{"yes", "no"}, NONE) == 0;
    }

    public void close() {
        Thread.currentThread().interrupt();
    }

    /**
     * source: <a href="https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream">https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream</a>
     */
    public void serialize() {
        XStream xstream = new XStream(new DomDriver());
        String xml = xstream.toXML(this);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("myfilename.xml");
            fos.write("<?xml version=\"1.0\"?>".getBytes(StandardCharsets.UTF_8)); //write XML header, as XStream doesn't do that for us
            byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);

        } catch (Exception e) {
            e.printStackTrace(); // this obviously needs to be refined.
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace(); // this obviously needs to be refined.
                }
            }
        }
    }

    /**
     * source: <a href="https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream">https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream</a>
     */
    public ComponentAssembler deserialize() {
        XStream xstream = new XStream(new DomDriver());
        ComponentAssembler componentAssembler = new ComponentAssembler(); //if there is an error during deserialization, this is going to be returned, is this what you want?
        try {
            File xmlFile = new File("myfilename.xml");
            componentAssembler = (ComponentAssembler) xstream.fromXML(xmlFile);
        } catch (Exception e) {
            System.err.println("Error in XML Read: " + e.getMessage());
        }
        return componentAssembler;
    }
}
