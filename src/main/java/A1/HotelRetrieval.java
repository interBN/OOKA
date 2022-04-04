package A1;

import A1.interfaces.HotelSearch;
import A1.types.Hotel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelRetrieval implements HotelSearch {

    private static final HotelRetrieval singleton = new HotelRetrieval();

    private final Cache cache;
    private final DBAccess db;

    private final Logger logger;

    private HotelRetrieval() {
        this.cache = Cache.getInstance();
        this.db = DBAccess.getInstance();
        this.logger = Logger.getInstance();
    }

    public static HotelRetrieval getInstance() {
        return singleton;
    }

    @Override
    public Hotel[] getHotelByName(String name) throws Exception {
        long start = System.currentTimeMillis();

        List<Object> result = cache.getObjects(DBAccess.HOTEL, name); // check if name is in cache

        // colors: https://www.geeksforgeeks.org/how-to-print-colored-text-in-java-console/
        String log = String.format("Zugriff auf Buchungssystem über Methode getHotelByName(), \u001B[32mSuchwort: \"%s\", \u001B[33mMethode: ", name);
        if (result != null) {
            log += "CACHE";
        } else { // if no: fetch hotels by name
            openSession();
            result = db.getObjects(DBAccess.HOTEL, name);
            cache.cacheResult(name, result);
            db.closeConnection();
            log += "FETCH";
        }
        Hotel[] hotels = ListToHotels(result);

        long finish = System.currentTimeMillis();
        long timeElapsed = finish - start;
        log += String.format(", \u001B[34mDAUER: %d ms, ", timeElapsed);
        log += String.format("\u001B[35mANTWORT: %s", Arrays.toString(hotels));
        log += "\u001B[0m";
        logger.log(log);

        return hotels;
    }

    @Override
    public void openSession() {
        db.openConnection();
    }

    private Hotel[] ListToHotels(List<Object> list) throws Exception {
        if (list.size() % 3 != 0) {
            throw new Exception("Something is wrong with the API response. Some data are missing: " + list);
        }
        List<Hotel> result = new ArrayList<>();
        for (int i = 0, resultSize = list.size() / 3; i < resultSize; i++) {
            int index = i * 3;
            result.add(new Hotel(list.subList(index, index + 3)));
        }
        return result.toArray(new Hotel[0]);
    }
}