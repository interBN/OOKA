import org.junit.Assert;
import org.junit.Test;

public class PrimeTesterTest {

    @Test
    public void isPrime() {
        PrimeTester p = new PrimeTester();
        boolean prime = p.isPrime(1);
        Assert.assertFalse(prime);
    }
}