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

    private final String componentPath;
    private final String selectedComponent;
    private final String selectedClass;
    private final String selectedMethod;
    private final int selectedMethodIndex;

    private final long created;
    private final int id;
    private boolean active;
    private long activeTime;

    private URLClassLoader classLoader;
    private boolean block = false;


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

    boolean isBlock() {
        return block;
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
