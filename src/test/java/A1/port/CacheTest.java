package A1.port;

import A1.types.Hotel;
import org.junit.Assert;
import org.junit.Test;

public class CacheTest {

    @Test
    public void getInstance() {
        Cache instance = Cache.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void getInstance2() {
        Cache instance = Cache.getInstance();
        Cache instance2 = Cache.getInstance();
        Assert.assertSame(instance, instance2);
    }

    @Test
    public void cacheResult() throws Exception {
        Cache instance = Cache.getInstance();
        Hotel[] h = new Hotel[1];
        h[0] = new Hotel(1, "name", "city");
        instance.cacheResult("test", h);
        Hotel[] h2 = instance.getHotels("test");
        Assert.assertSame(h, h2);
    }

    @Test
    public void getHotelsProxy() {
        Cache instance = Cache.getInstance();
        Hotel[] h = new Hotel[1];
        h[0] = new Hotel(1, "name", "city");
        instance.cacheResult("test2", h);
        Hotel[] h2 = instance.getHotelsProxy("test2");
        Assert.assertSame(h, h2);
    }
}