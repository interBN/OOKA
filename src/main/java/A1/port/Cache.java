package A1.port;

import A1.interfaces.Caching;
import A1.types.Hotel;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Cache extends Proxy implements Caching {

    private static final Cache singleton = new Cache();

    private final Map<String, Hotel[]> cache;

    private Cache() {
        super("CACHE");
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
    protected Hotel[] getHotels(int type, String name) {
        return cache.get(name);
    }
}