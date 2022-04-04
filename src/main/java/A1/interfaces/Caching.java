package A1.interfaces;

import A1.types.Hotel;

public interface Caching extends Port {

    void cacheResult(String key, Hotel[] value);
}
