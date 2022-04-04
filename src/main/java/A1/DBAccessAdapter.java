package A1;

import A1.interfaces.DataProvider;
import A1.types.Hotel;

import java.util.ArrayList;
import java.util.List;

public class DBAccessAdapter implements DataProvider {

    private static final DBAccessAdapter singleton = new DBAccessAdapter();

    private final DBAccess db;

    private DBAccessAdapter() {
        db = new DBAccess();
    }

    public static DBAccessAdapter getInstance() {
        return singleton;
    }


    @Override
    public Hotel[] getObjects(int type, String value) throws Exception {
        db.openConnection();
        List<String> response = db.getObjects(type, value);
        db.closeConnection();
        return listToHotels(response);
    }

    public Hotel[] listToHotels(List<String> list) throws Exception {
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
