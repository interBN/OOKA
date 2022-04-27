package A2;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class Helper {
    static String getLine() {
        return "----------------------------------------";
    }

    static long now() {
        return new Date().getTime();
    }

    static String msToDate(long ms) {
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
}
