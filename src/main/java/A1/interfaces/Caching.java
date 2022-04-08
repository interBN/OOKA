package A1.interfaces;

import A1.types.Hotel;

/**
 * @author Siu Cheng
 * @author Veronika Seidel
 */
public interface Caching {

    void cacheResult(String key, Hotel[] value);
}
