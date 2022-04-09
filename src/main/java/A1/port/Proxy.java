package A1.port;

import A1.types.Hotel;

import java.util.Arrays;
import java.util.Locale;

/**
 * @author Siu Cheng
 * @author Veronika Seidel
 */
public abstract class Proxy {

    private final Logger logger;
    private final String method;

    public Proxy(String method) {
        this.logger = Logger.getInstance();
        this.method = method;
    }

    @SuppressWarnings("SameParameterValue")
    abstract Hotel[] getHotelsInProxy(String name) throws Exception;

    public Hotel[] getHotels(String name) throws Exception {

        long start = System.currentTimeMillis();
        String log = String.format("Zugriff auf Buchungssystem über Methode getHotelByName(), \u001B[32mSuchwort: \"%s\", \u001B[33mMethode: %s", name, method);

        name = name.toLowerCase(Locale.ROOT);
        Hotel[] hotels = getHotelsInProxy(name);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        log += String.format(", \u001B[34mDAUER: %d ms, ", timeElapsed);
        log += String.format("\u001B[35mANTWORT: %s\u001B[0m", Arrays.toString(hotels));
        logger.log(log);

        return hotels;
    }
}