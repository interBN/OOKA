package A1;

import A1.interfaces.HotelSearch;
import A1.port.Cache;
import A1.port.DBAccessProxy;
import A1.port.Proxy;
import A1.types.Hotel;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Siu Cheng
 * @author Veronika Seidel
 * @see HotelRetrieval is the major class to call from outside
 */
public class HotelRetrieval implements HotelSearch {

    private static final HotelRetrieval singleton = new HotelRetrieval();

    /**
     * @see HotelRetrieval#proxies lists all proxies that provide data.
     * The lower the index the higher the priotity.
     */
    private final List<Proxy> proxies;

    private HotelRetrieval() {
        proxies = new ArrayList<>();
        proxies.add(Cache.getInstance());
        proxies.add(DBAccessProxy.getInstance());
    }

    public static HotelRetrieval getInstance() {
        return singleton;
    }

    /**
     * @param name name of the hotel
     * @return list of hotels with substring of name
     */
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