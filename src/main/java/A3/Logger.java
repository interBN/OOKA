package A3;

import A2.Helper;

public class Logger {
    public Logger() {
    }

    public void sendLog(String msg) {
        System.out.println("++++ LOG: " + msg + " (" + Helper.msToDate(Helper.now()) + ")");
    }
}
