package A3;

import java.time.LocalDateTime;

public class Logger {
    public Logger(){
    }
    public void sendLog(String msg){
        System.out.println("++++ LOG: " + msg + "(" + LocalDateTime.now() + ")");
    }
}
