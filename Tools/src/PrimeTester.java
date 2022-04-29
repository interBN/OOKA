import A2.StartClassDeclaration;
import A2.StartMethodDeclaration;
import A2.StopMethodDeclaration;

import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

@SuppressWarnings("unused")
@StartClassDeclaration

public class PrimeTester {

    Map<Integer, Boolean> cache;

    public PrimeTester() {
        this.cache = new TreeMap<>();
    }

    @StartMethodDeclaration
    public static void main(String[] args) {
        PrimeTester prime = new PrimeTester();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Prime Tester");
        System.out.println("Enter \"exit\" to leave");

        while (true) {
            System.out.print("Please enter number: ");
            String input = scanner.next();
            if (input.toLowerCase(Locale.ROOT).equals("exit")) {
                break;
            }
            boolean isPrime = prime.isPrime(Integer.parseInt(input));
            System.out.println(input + (isPrime ? " is prime." : " is not prime."));
        }
    }

    @StartMethodDeclaration
    public static PrimeTester getInstance() {
        return new PrimeTester();
    }

    @StopMethodDeclaration
    public void kill() {
        Thread.currentThread().interrupt();
    }

    public boolean isPrime(int num) {
        Boolean exist = cache.get(num);
        if (exist != null) {
            System.out.println("Result from cache.");
            return exist;
        }

        boolean result = true;
        if (num <= 1) {
            result = false;
        } else {
            for (int i = 2; i <= Math.sqrt(num); i++) {
                if (num % i == 0) {
                    result = false;
                    break;
                }
            }
        }
        cache.put(num, result);
        return result;
    }
}
