package A1;

import A1.interfaces.Caching;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@SuppressWarnings("unused")
public class Cache implements Caching {

    private static final Cache singleton = new Cache();

    private final Map<String, List<Object>> cache;

    private Cache() {
        this.cache = new TreeMap<>();
    }

    public static Cache getInstance() {
        return singleton;
    }

    public void cacheResult(String key, List<Object> value) {
        cache.put(key, value);
    }

    @Override
    public List<Object> getObjects(int type, String value) {
        return cache.get(value);
    }
}