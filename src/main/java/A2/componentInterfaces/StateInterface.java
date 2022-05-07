package A2.componentInterfaces;

import A2.annotations.StopMethodDeclaration;

public interface StateInterface {

    void nextState();

    String getCurrentState();

    void log(String msg);

    @StopMethodDeclaration
    void kill();


}
