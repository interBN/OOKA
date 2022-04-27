package A2;

import static A2.Helper.*;

public class Component implements Runnable {

    private final String componentPath;
    private final String selectedComponent;
    private final String selectedClass;
    private final String selectedMethod;
    private final long created;
    private final int id;
    private long activeStart;
    private boolean active;


    public Component(String componentPath, String selectedComponent, String selectedClass, String selectedMethod) {
        this.componentPath = componentPath;
        this.selectedComponent = selectedComponent;
        this.selectedClass = selectedClass;
        this.selectedMethod = selectedMethod;
        this.activeStart = -1;
        this.created = now();
        this.active = false;
        this.id = toString().hashCode() & 0xfffffff;
    }

    @Override
    public void run() {
        startInit();
    }

    void startInit() {

        System.out.println(this.componentPath);
        System.out.println(this.selectedComponent);
        System.out.println(this.selectedClass);
        System.out.println(this.selectedMethod);

        System.out.println("Going for sleep for 5 secs");
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Resumed after 5 secs");

        activeStart = now();
        active = true;
    }

    void stopInit() {
        activeStart = -1;
        active = false;
    }

    @Override
    public String toString() {
        String n = "\n";
        long[] duration = msToDuration(now() - activeStart);
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
                (active ? n + "active time: " + h + ":" + m + ":" + s : "");
    }
}
