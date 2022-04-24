import org.junit.Assert;
import org.junit.Test;

public class PrimeTesterTest {

    @Test
    public void isPrime() {
        boolean prime = PrimeTester.isPrime(1);
        Assert.assertFalse(prime);
    }
}