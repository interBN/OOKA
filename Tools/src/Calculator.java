import A2.annotations.StartClassDeclaration;
import A2.annotations.StartMethodDeclaration;
import A3.Inject;
import A3.Logger;

import java.util.Scanner;

//@StartClassDeclaration
public class Calculator {

//    @Inject
    static Logger logger = null;

//    @StartMethodDeclaration
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Please enter number: ");
        int a = scanner.nextInt();
        System.out.print("Please enter another number: ");
        int b = scanner.nextInt();
        System.out.println("a + b = " + add(a, b));

        logger.sendLog("vlasd");
    }

    static int add(int a, int b) {
        return a + b;
    }
}