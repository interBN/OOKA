package A1;

import A1.interfaces.Caching;
import A1.types.Hotel;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Cache implements Caching {

    private static final Cache singleton = new Cache();

    private final Map<String, Hotel[]> cache;

    private Cache() {
        this.cache = new HashMap<>();
    }

    public static Cache getInstance() {
        return singleton;
    }

    @Override
    public void cacheResult(String key, Hotel[] value) {
        cache.put(key, value);
    }

    @Override
    public Hotel[] getObjects(int type, String value) {
        return cache.get(value);
    }
}