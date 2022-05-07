import A2.annotations.StartClassDeclaration;
import A2.annotations.StartMethodDeclaration;
import A2.annotations.StopMethodDeclaration;
import A3.Inject;
import A3.Logger;

@StartClassDeclaration
public class State {
    @Inject
    public static Logger logger;
    static boolean kill;
    static States currentState;

    @StartMethodDeclaration
    public static void main(String[] args) {
        kill = false;
        currentState = States.STATE_A;
        logger.sendLog("Start State");

        if (!kill) {
            try {
                Thread.sleep(1000);
                if (logger == null) {
                    System.out.println("Injection did not work.");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void nextState() {
        if (currentState == States.STATE_A) {
            currentState = States.STATE_B;
        } else if (currentState == States.STATE_B) {
            currentState = States.STATE_C;
        } else if (currentState == States.STATE_C) {
            currentState = States.STATE_A;
        }
    }

    @StopMethodDeclaration
    enum States {
        STATE_A, STATE_B, STATE_C,
    }
}