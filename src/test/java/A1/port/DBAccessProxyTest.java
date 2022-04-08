package A1.port;

import A1.types.Hotel;
import org.junit.Assert;
import org.junit.Test;

public class DBAccessProxyTest {

    @Test
    public void getInstance() {
        DBAccessProxy instance = DBAccessProxy.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void getInstance2() {
        DBAccessProxy instance = DBAccessProxy.getInstance();
        DBAccessProxy instance2 = DBAccessProxy.getInstance();
        Assert.assertSame(instance, instance2);
    }

    @Test
    public void getHotelsProxy() throws Exception {
        DBAccessProxy instance = DBAccessProxy.getInstance();
        Hotel[] h = instance.getHotelsProxy("test");
        Assert.assertEquals(0, h.length);
    }
}