package A1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.util.List;

public class DBAccessTest {

    DBAccess acc;

    @BeforeEach
    public void testOpenConnection() {
        acc = new DBAccess();
        acc.openConnection();
    }

    @Test
    public void testGetObjects() {
        System.out.println("Test GetObjects: Search for \"Maritim\"");
        List<String> hotels = acc.getObjects(DBAccess.HOTEL, "maritim");
        for (String s : hotels)
            System.out.println(s);
        Assertions.assertEquals(3, hotels.size());
    }

    @AfterEach
    public void testCloseConnection() {
        acc.closeConnection();
    }

}
