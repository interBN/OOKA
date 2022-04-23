import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter number: ");
        int a = scanner.nextInt();
        System.out.print("Please enter another number: ");
        int b = scanner.nextInt();
        System.out.println("a + b = " + (a + b));
    }
}