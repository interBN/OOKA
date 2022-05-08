package A2;

import A2.annotations.StartClassDeclaration;
import A3.VersionControl;
import com.thoughtworks.xstream.annotations.XStreamOmitField;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

import static A2.Helper.now;

public class ComponentAssembler implements Runnable {

    static ComponentAssembler singleton = null;
    //    boolean killInstance = false;
//    final long created;
    VersionControl versionControl;
    @XStreamOmitField
    @SuppressWarnings("FieldCanBeLocal")
    Map<Thread, Component> components;

    long created;
    boolean killInstance = false;

    private ComponentAssembler() {
        this.components = new HashMap<>();
        this.versionControl = new VersionControl("src/main/java/A2/saves", 3);
        this.created = now();
    }

    public static ComponentAssembler getInstance() {
        if (singleton == null) {
            singleton = new ComponentAssembler();
        }
        return singleton;
    }

    @Override
    public void run() {
        ComponentAssembler instance = getInstance();
        while (!killInstance) {
            try {
                Thread.sleep(1000);
//                System.out.println("bla");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    void printStatus() {
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

    void loadComponent(Component component) throws IOException, ClassNotFoundException, IllegalAccessException, InvocationTargetException, InterruptedException {
        Thread thread = new Thread(component);
        this.components.put(thread, component);
        System.out.println(Helper.getLine());
        System.out.println("A new thread has been created.");
        System.out.println(component);
    }

    void unloadComponent(Component component) {
        Thread thread = findThread(component);
        assert thread != null;
        component.close();
        thread.interrupt();
        components.remove(thread);
    }

    void startComponent(Component component) throws ClassNotFoundException, InvocationTargetException, IllegalAccessException, NoSuchMethodException, MalformedURLException, InterruptedException {
        Thread thread = findThread(component);
        this.components.remove(thread);
        Thread newThread = new Thread(component);
        this.components.put(newThread, component);
        newThread.start();
        newThread.join();
    }

    void stopComponent(Component component) throws IOException {
        component.stopInit();
    }

    private Thread findThread(Component component) {
        Thread thread = null;
        for (Map.Entry<Thread, Component> entry : components.entrySet()) {
            Thread key = entry.getKey();
            Component value = entry.getValue();
            if (value.equals(component)) {
                return key;
            }
        }
        return thread;
    }

    String[] getJarFiles(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles())).filter(file -> !file.isDirectory() && file.getName().endsWith(".jar")).map(File::getName).toArray(String[]::new);
    }

    String[] listAllThreads(String prefix) {
        List<String> optionsList = new ArrayList<>();
        components.forEach((key, component) -> {
            String line = prefix + Helper.BLUE + component.getId() + Helper.ANSI_RESET + "#" + Helper.YELLOW + component.getSelectedComponent() + Helper.ANSI_RESET + "#" + Helper.RED + component.isActive() + Helper.ANSI_RESET;
            optionsList.add(line);
        });
        return optionsList.toArray(String[]::new);
    }

    /**
     * source: <a href="https://stackoverflow.com/a/11016392">https://stackoverflow.com/a/11016392</a>
     */
    String[] getClassNamesFromJarFile(String dir) throws IOException, ClassNotFoundException {
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

    Constructor<?>[] getContructors(String classArg, String dir) throws MalformedURLException, ClassNotFoundException {
        URL url = new File(dir).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class<?> c = cl.loadClass(classArg);
        return c.getConstructors();

        //        Method[] methods = c.getDeclaredMethods();
//        List<String> methodsFiltered = new ArrayList<>();
//        for (Method method : methods) {
//            StartMethodDeclaration annotation2 = method.getDeclaredAnnotation(StartMethodDeclaration.class);
//            if (annotation2 != null) {
//                methodsFiltered.add(method.getName());
//            }
//        }
////        c.getMethod("main", String[].class)
//        return constructors;
    }

    public void close() {
        killInstance = true;
        Thread.currentThread().interrupt();
    }

    void serializeComponents() {
        Class<?>[] classes = new Class[]{Component.class};
        versionControl.serialize(components.values().toArray(), classes);
    }
}
