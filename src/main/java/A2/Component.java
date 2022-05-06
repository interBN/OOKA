package A2;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import static A2.Helper.*;

public class Component implements Runnable {

    final String componentPath;
    final String selectedComponent;
    final String selectedClass;
    final String selectedMethod;
    final int selectedMethodIndex;
    final long created;
    final int id;
    boolean active;
    long activeTime;

    URLClassLoader classLoader;
    boolean block = false;

    public Component(String componentPath, String selectedComponent, String selectedClass, String selectedMethod, int selectedMethodIndex) {
        this.componentPath = componentPath;
        this.selectedComponent = selectedComponent;
        this.selectedClass = selectedClass;
        this.selectedMethod = selectedMethod;
        this.selectedMethodIndex = selectedMethodIndex;
        this.created = now();
        this.active = false;
        this.activeTime = -1;
        this.id = toString().hashCode() & 0xfffffff;
    }

    @Override
    public void run() {
        try {
            startInit();
        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException | ClassNotFoundException |
                 MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    void startInit() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, ClassNotFoundException, MalformedURLException {
        block = true;
        URL url = new File(componentPath + selectedComponent).toURL();
        classLoader = new URLClassLoader(new URL[]{url});
        Class<?> c = classLoader.loadClass(this.selectedClass);
        Method method = c.getDeclaredMethods()[this.selectedMethodIndex];
        String[] args = {};
        method.invoke(null, (Object) args);
        activeTime = now();
        active = true;
        block = false;
    }

    void stopInit() {
        try {
            classLoader.close();
        } catch (IOException ignore) {
            // throw new RuntimeException(e);
        }
        activeTime = -1;
        active = false;
    }

//    public String[] getClasses() throws IOException {
//        String dir = getComponentPath() + getSelectedComponent();
//        Set<String> set = new HashSet<>();
//        JarFile jarFile = new JarFile(dir);
//        Enumeration<JarEntry> e = jarFile.entries();
//        while (e.hasMoreElements()) {
//            JarEntry je = e.nextElement();
//            if (je.isDirectory() || !je.getName().endsWith(".class")) {
//                continue;
//            }
//            String className = je.getName().substring(0, je.getName().length() - 6);
//            set.add(className.replace('/', '.'));
//        }
//        return set.toArray(new String[0]);
//    }
//
//    public String[] getMethods(String className) throws IOException, ClassNotFoundException {
//        String dir = getComponentPath() + getSelectedComponent();
//        URL url = new File(dir).toURL();
//        URLClassLoader cl = new URLClassLoader(new URL[]{url});
//        Class<?> c = cl.loadClass(className);
//        Method[] methods = c.getDeclaredMethods();
//        return Arrays.stream(methods).map(Method::getName).toArray(String[]::new);
//    }

    @Override
    public String toString() {
        String n = "\n";
        long[] duration = msToDuration(now() - activeTime);
        String h = active ? String.valueOf(duration[0]) : "00";
        String m = active ? String.valueOf(duration[1]) : "00";
        String s = active ? String.valueOf(duration[2]) : "00";
        if (h.length() == 1) h = "0" + h;
        if (m.length() == 1) m = "0" + m;
        if (s.length() == 1) s = "0" + s;
        return "id: " + id + n
                + "component: " + selectedComponent + n
                + "class: " + selectedClass + n
                + "method: " + selectedMethod + n
                + "created: " + msToDate(created) + n
                + "active: " + active +
                (active ? n + "duration: " + h + ":" + m + ":" + s : "");
    }

    public void close() {
        Thread.currentThread().interrupt();
    }

    boolean isBlock() {
        return block;
    }

    public String getComponentPath() {
        return componentPath;
    }

    public String getSelectedComponent() {
        return selectedComponent;
    }

    public String getSelectedClass() {
        return selectedClass;
    }

    public String getSelectedMethod() {
        return selectedMethod;
    }

    public long getCreated() {
        return created;
    }

    public int getId() {
        return id;
    }

    public long getActiveTime() {
        return activeTime;
    }

    public boolean isActive() {
        return active;
    }
}
