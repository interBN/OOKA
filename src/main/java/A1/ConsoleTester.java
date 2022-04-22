package A1;

import java.util.Locale;
import java.util.Scanner;

/**
 * @author Siu Cheng
 * @author Veronika Seidel
 */
public class ConsoleTester {
    public static void main(String[] args) {
        System.out.println("HOTEL SEARCH");
        System.out.println("Info: enter 'exit' to leave");
        Scanner scanner = new Scanner(System.in);
        HotelRetrieval h = HotelRetrieval.getInstance();
        while (true) {
            System.out.println("-----------------------------------------");
            System.out.print("Search for hotel name: ");
            String input = scanner.next();
            if (input.toLowerCase(Locale.ROOT).equals("exit")) {
                break;
            }
            try {
                h.getHotelByName(input);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}