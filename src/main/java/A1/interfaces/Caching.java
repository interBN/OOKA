package A1.interfaces;

import java.util.List;

public interface Caching extends Data {

    void cacheResult(String key, List<Object> value);
}
