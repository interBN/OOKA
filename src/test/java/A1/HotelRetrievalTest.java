package A1;

import A1.types.Hotel;
import org.junit.Assert;
import org.junit.Test;

public class HotelRetrievalTest {

    @Test
    public void getInstance() {
        HotelRetrieval instance = HotelRetrieval.getInstance();
        Assert.assertNotNull(instance);
    }

    @Test
    public void getInstance2() {
        HotelRetrieval instance = HotelRetrieval.getInstance();
        HotelRetrieval instance2 = HotelRetrieval.getInstance();
        Assert.assertSame(instance, instance2);
    }

    @Test
    public void getHotelByName0() {
        HotelRetrieval hr = HotelRetrieval.getInstance();
        String search = "*";
        try {
            Hotel[] h = hr.getHotelByName(search);
            Assert.assertEquals(10, h.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHotelByName1() {
        HotelRetrieval hr = HotelRetrieval.getInstance();
        String search = "ma";
        try {
            Hotel[] h = hr.getHotelByName(search);
            Assert.assertEquals(3, h.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHotelByName2() {
        HotelRetrieval hr = HotelRetrieval.getInstance();
        String search = "a64ds6153das4153d5d46a5";
        try {
            Hotel[] h = hr.getHotelByName(search);
            Assert.assertEquals(0, h.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getHotelByName3() {
        HotelRetrieval hr = HotelRetrieval.getInstance();
        String search = "ma";
        try {
            Hotel[] h1 = hr.getHotelByName(search);
            System.out.println("------------------------------------------");
            Hotel[] h2 = hr.getHotelByName(search);
            Assert.assertArrayEquals(h1, h2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}