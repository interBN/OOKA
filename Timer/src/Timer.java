import A2.annotations.StartClassDeclaration;
import A2.annotations.StartMethodDeclaration;
import A2.annotations.StopMethodDeclaration;
import A3.Inject;
import A3.Logger;

@StartClassDeclaration
public class Timer {

    @Inject
    public static Logger logger;
    static boolean kill;
    static int counter;

    public Timer() {
        kill = false;
        counter = 0;
    }

    @StartMethodDeclaration
    public static void main(String[] args) {
        Timer t = new Timer();
        run();
    }

    static void run() {
        while (!kill) {
            try {
                Thread.sleep(1000);
                if (logger != null) {
                    logger.sendLog(String.valueOf(counter++));
                } else {
                    System.out.println("Injection did not work.");
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
