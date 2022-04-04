package A1;

import A1.interfaces.DataProvider;
import A1.interfaces.HotelSearch;
import A1.types.Hotel;

import java.util.Arrays;

public class HotelRetrieval implements HotelSearch {

    private static final HotelRetrieval singleton = new HotelRetrieval();

    private final Cache cache;
    private final DBAccessAdapter db;

    private final Logger logger;

    private HotelRetrieval() {
        this.cache = Cache.getInstance();
        this.db = DBAccessAdapter.getInstance();
        this.logger = Logger.getInstance();
    }

    public static HotelRetrieval getInstance() {
        return singleton;
    }

    @Override
    public Hotel[] getHotelByName(String name) throws Exception {

        long start = System.currentTimeMillis();
        String log = String.format("Zugriff auf Buchungssystem über Methode getHotelByName(), \u001B[32mSuchwort: \"%s\", \u001B[33mMethode: ", name);

        Hotel[] hotels = cache.getObjects(DBAccess.HOTEL, name);
        if (hotels != null) {
            log += "CACHE";
        } else {
            hotels = db.getObjects(DBAccess.HOTEL, name);
            cache.cacheResult(name, hotels);
            log += "FETCH";
        }

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        log += String.format(", \u001B[34mDAUER: %d ms, ", timeElapsed);
        log += String.format("\u001B[35mANTWORT: %s", Arrays.toString(hotels));
        log += "\u001B[0m";
        logger.log(log);

        return hotels;
    }

    @Override
    @Deprecated
    public void openSession() {
    }


}