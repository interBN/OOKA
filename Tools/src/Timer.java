import A2.annotations.StartClassDeclaration;
import A2.annotations.StartMethodDeclaration;
import A2.annotations.StopMethodDeclaration;
import A3.Inject;
import A3.Logger;

@StartClassDeclaration
public class Timer {

    @Inject
    public Logger logger;
    boolean kill;

    public Timer() {
        this.kill = false;
    }

    @StartMethodDeclaration
    public static void main(String[] args) {
        Timer t = new Timer();
        t.run();
    }

    void run() {
        while (!kill) {
            try {
                Thread.sleep(1000);
//                System.out.println("aiujsfdhiahf");
                if (logger != null) {
                    logger.sendLog("alskifdjoiasdlfj");
                } else {
                    System.out.println("Injection does not work.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @StopMethodDeclaration
    void kill() {
        kill = true;
    }
}
