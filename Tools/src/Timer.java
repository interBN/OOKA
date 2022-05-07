import A2.annotations.StartClassDeclaration;
import A2.annotations.StartMethodDeclaration;
import A2.annotations.StopMethodDeclaration;

@StartClassDeclaration
public class Timer {

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
                System.out.println("aiujsfdhiahf");
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
