package A1;

import A1.interfaces.HotelSearch;
import A1.types.Hotel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HotelRetrieval implements HotelSearch {

    private final Cache cache;
    private final DBAccess db;

    public HotelRetrieval() {
        this.cache = Cache.getInstance();
        this.db = DBAccess.getInstance();
    }


    public Hotel[] getHotelByName(String name) throws Exception {
        List<Object> result = cache.getObjects(DBAccess.HOTEL, name); // check if name is in cache
        if (result != null) {
            System.out.print("Found hotel in cache: ");
        } else { // if no: fetch hotels by name
            openSession();
            result = db.getObjects(DBAccess.HOTEL, name);
            cache.cacheResult(name, result);
            db.closeConnection();
            System.out.print("Fetch hotel: ");
        }
        Hotel[] hotels = ListToHotels(result);
        System.out.println(Arrays.toString(hotels));
        return hotels;
    }

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