package A1.port;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

/**
 * @author Siu Cheng
 * @author Veronika Seidel
 */
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

    /**
     * Can be called from the debugger.
     */
    @SuppressWarnings("unused")
    public void print() {
        for (Long key : logs.keySet()) {
            System.out.println(key + ": " + logs.get(key));
        }
    }

    /**
     * @param ms 1649416349056L
     * @return "08.04.22 13:12"
     */
    public String msToDate(long ms) {
        Date currentDate = new Date(ms);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy HH:mm");
        return sdf.format(currentDate);
    }
}
