package A1.port;

import A1.types.Hotel;

import java.util.ArrayList;
import java.util.List;

public class DBAccessProxy extends Proxy {

    private static final DBAccessProxy singleton = new DBAccessProxy();

    private final DBAccess db;

    private DBAccessProxy() {
        super("FETCH");
        db = new DBAccess();
    }

    public static DBAccessProxy getInstance() {
        return singleton;
    }

    @Override
    protected Hotel[] getHotels(int type, String name) throws Exception {
        db.openConnection();
        List<String> response = db.getObjects(type, name);
        db.closeConnection();
        Hotel[] hotels = listToHotels(response);
        Cache.getInstance().cacheResult(name, hotels);
        return hotels;
    }

    private Hotel[] listToHotels(List<String> list) throws Exception {
        if (list == null) {
            return null;
        }
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
