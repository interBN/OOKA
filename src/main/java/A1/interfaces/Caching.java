package A1.interfaces;

import A1.types.Hotel;

public interface Caching extends DataProvider {

    void cacheResult(String key, Hotel[] value);
}
