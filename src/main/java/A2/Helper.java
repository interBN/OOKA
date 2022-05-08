package A2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

@SuppressWarnings("unused")
public class Helper {
    static final String BLACK = "\u001B[30m";
    static final String RED = "\u001B[31m";
    static final String GREEN = "\u001B[32m";
    static final String YELLOW = "\u001B[33m";
    static final String BLUE = "\u001B[34m";
    static final String CYAN = "\u001B[36m";
    static final String WHITE = "\u001B[37m";
    static final String PURPLE = "\u001B[35m";
    static final String ANSI_RESET = "\u001B[0m";

    static String getLine() {
        return "----------------------------------------";
    }

    public static long now() {
        return new Date().getTime();
    }

    public static String msToDate(long ms) {
        DateFormat simple = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss:SSS Z");
        Date result = new Date(ms);
        return simple.format(result);
    }

    static long[] msToDuration(long ms) {
        Duration duration = Duration.ofMillis(ms);
        long seconds = duration.getSeconds();
        long h = seconds / 3600;
        long m = (seconds % 3600) / 60;
        long s = seconds % 60;
        return new long[]{h, m, s};
    }

    enum Breaker {
        BACK, EXIT, NONE
    }
}
