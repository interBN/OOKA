package A1.port;

import org.junit.Assert;
import org.junit.Test;

public class LoggerTest {

    @Test
    public void getInstance() {
        Logger instance = Logger.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void getInstance2() {
        Logger instance = Logger.getInstance();
        Logger instance2 = Logger.getInstance();
        Assert.assertSame(instance, instance2);
    }

    @Test
    public void msToDate() {
        Logger instance = Logger.getInstance();
        String date = instance.msToDate(1649416349056L);
        Assert.assertEquals("08.04.22 13:12", date);
    }
}