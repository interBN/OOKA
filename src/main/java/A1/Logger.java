package A1;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

public class Logger {

    private static Logger instance = null;

    private final Map<Long, String> logs;

    private Logger() {
        this.logs = new TreeMap<>();
    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String msg) {
        log(msg, true);
    }

    public void log(String msg, boolean print) {
        Date date = new Date();
        long ms = date.getTime();
        logs.put(ms, msg);
        if (print) {
            System.out.println(msToDate(ms) + ": " + msg);
        }
    }

    public void print() {
        for (Long key : logs.keySet()) {
            System.out.println(key + ": " + logs.get(key));
        }
    }

    public String msToDate(long ms) {
        Date currentDate = new Date(ms);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
        String format = sdf.format(currentDate);
        return format;
    }
}
