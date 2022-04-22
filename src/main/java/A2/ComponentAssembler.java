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
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComponentAssembler {

    static final String pathResources = "src/main/java/A2/resources/";
    private final Scanner sc;

    public ComponentAssembler() {
        sc = new Scanner(System.in);
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

    private void loadJar() throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {

        // SELECT COMPONENT
        String[] components = listJarFiles(pathResources).toArray(new String[0]);
        String selectedComponent = ask("Please select a component:", components);

        // SELECT CLASS
        String[] classes = getClassNamesFromJarFile(pathResources + "/" + components[Integer.parseInt(selectedComponent)])
                .toArray(new String[0]);
        String selectedClass = ask("Please select a class:", classes);

        // SELECT METHOD
        String pathComponent = pathResources + "/" + selectedComponent;
        String[] methods = getMethods(classes, selectedClass, pathResources + "/" + selectedComponent);
        String selectedMethod = ask("Please select method:", methods);

        // RUN METHOD
        runMethod(classes, selectedClass, pathComponent, selectedMethod);
    }

    private void runMethod(String[] classes, String selectedClass, String pathComponent, String selectedMethod) throws MalformedURLException, ClassNotFoundException, IllegalAccessException, InvocationTargetException {
        URL url = new File(pathComponent).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class<?> c = cl.loadClass(classes[Integer.parseInt(selectedClass)]);
        Method method = c.getDeclaredMethods()[Integer.parseInt(selectedMethod)];
        String[] args = {"arg1", "arg2"};
        // Object instance = c.newInstance();
        // Object result =
        method.invoke(null, (Object) args);
    }

    private String[] getMethods(String[] classes, String selectedClass, String pathComponent) throws MalformedURLException, ClassNotFoundException {
        URL url = new File(pathComponent).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class<?> c = cl.loadClass(classes[Integer.parseInt(selectedClass)]);
        Method[] methods = c.getDeclaredMethods();
        return Arrays.stream(methods).map(Method::getName).toArray(String[]::new);
    }

    public Set<String> listJarFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory() && file.getName().endsWith(".jar"))
                .map(File::getName)
                .collect(Collectors.toSet());
    }

    public Set<String> getClassNamesFromJarFile(String dir) throws IOException, ClassNotFoundException {
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
        return set;
    }

    public void run() throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException, IllegalAccessException {
        System.out.println("Start Component Assembler");
        while (true) {
            System.out.println("------------------------------------------");
            String[] msg = {
                    "show status",
                    "run component",
                    "stop component"
            };
            String input = ask("Please select number:", msg);
            if (input.equals("exit")) {
                break;
            } else if (input.equals("1")) {
                loadJar();
            }
        }
    }

    private String ask(String question, String[] options) {
        System.out.println(question);
        for (int i = 0; i < options.length; i++) {
            System.out.println("[" + i + "] " + options[i]);
        }
        return sc.nextLine().toLowerCase();
    }
}
