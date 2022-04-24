import org.junit.Assert;
import org.junit.Test;

public class CalculatorTest {

    @Test
    public void add() {
        int result = Calculator.add(2, 5);
        Assert.assertEquals(result, 7);
    }
}