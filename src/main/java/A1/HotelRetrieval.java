package A1;

import A1.interfaces.HotelSearch;
import A1.port.Cache;
import A1.port.DBAccessProxy;
import A1.port.Proxy;
import A1.types.Hotel;

import java.util.ArrayList;
import java.util.List;

public class HotelRetrieval implements HotelSearch {

    private static final HotelRetrieval singleton = new HotelRetrieval();

    private final List<Proxy> proxies;

    private HotelRetrieval() {
        proxies = new ArrayList<>();
        proxies.add(Cache.getInstance());
        proxies.add(DBAccessProxy.getInstance());
    }

    public static HotelRetrieval getInstance() {
        return singleton;
    }

    @Override
    public Hotel[] getHotelByName(String name) throws Exception {
        return getHotelByName(name, 0);
    }

    private Hotel[] getHotelByName(String name, int level) throws Exception {
        Hotel[] hotels = proxies.get(level).getHotels(name);
        if (hotels == null && level == proxies.size()) {
            return null;
        }
        if (hotels == null) {
            return getHotelByName(name, ++level);
        }
        return hotels;
    }
}