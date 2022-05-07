import A2.annotations.StartClassDeclaration;
import A2.annotations.StartMethodDeclaration;
import A2.annotations.StopMethodDeclaration;
import A2.componentInterfaces.StateInterface;
import A3.Inject;
import A3.Logger;

@StartClassDeclaration
public class State implements StateInterface {
    @Inject
    public Logger logger;
    boolean kill;
    States currentState;

    @StartMethodDeclaration
    public State() {
        kill = false;
        currentState = States.STATE_A;
//        log("Start State");

//        if (!kill) {
//            try {
//                Thread.sleep(1000);
//                if (logger == null) {
//                    System.out.println("Injection did not work.");
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Override
    public void nextState() {
        if (currentState == States.STATE_A) {
            currentState = States.STATE_B;
        } else if (currentState == States.STATE_B) {
            currentState = States.STATE_C;
        } else if (currentState == States.STATE_C) {
            currentState = States.STATE_A;
        }
    }

    @Override
    public String getCurrentState() {
        return currentState.name();
    }

    @Override
    public void log(String msg) {
        logger.sendLog(msg);
    }

    @Override
    @StopMethodDeclaration
    public void kill() {
        kill = true;
    }

    public enum States {
        STATE_A, STATE_B, STATE_C,
    }
}