package A2;

import java.util.Random;

public class Component implements Runnable {

    private final String componentPath;
    private final String selectedComponent;
    private final String selectedClass;
    private final String selectedMethod;
    private final long activeTimeMs;

    private final int id;
    //    private Runnable component;
    private boolean active;

    public Component(String componentPath, String selectedComponent, String selectedClass, String selectedMethod) {
        this.componentPath = componentPath;
        this.selectedComponent = selectedComponent;
        this.selectedClass = selectedClass;
        this.selectedMethod = selectedMethod;
        this.activeTimeMs = -1;


        Random r = new Random();
        String randomNumber = String.format("%04d", r.nextInt(1001));
        this.id = Integer.parseInt(randomNumber);
        this.active = false;
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


        active = true;
    }

    void stopInit() {
        active = false;
    }

    @Override
    public String toString() {
        return "Component{" +
                "componentPath='" + componentPath + '\'' +
                ", selectedComponent='" + selectedComponent + '\'' +
                ", selectedClass='" + selectedClass + '\'' +
                ", selectedMethod='" + selectedMethod + '\'' +
                ", activeTimeMs=" + activeTimeMs +
                ", id=" + id +
                ", active=" + active +
                '}';
    }
}
