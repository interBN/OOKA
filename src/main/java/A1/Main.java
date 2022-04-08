package A1;

import java.util.Locale;
import java.util.Scanner;

public class Main {
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
        scanner.close();
    }
}