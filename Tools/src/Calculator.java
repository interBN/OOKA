import java.util.Scanner;

public class Calculator {

    static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter number: ");
        int a = scanner.nextInt();
        System.out.print("Please enter another number: ");
        int b = scanner.nextInt();
        System.out.println("a + b = " + add(a, b));
    }

    static int add(int a, int b) {
        return a + b;
    }
}