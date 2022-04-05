package A1;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        HotelRetrieval hr = HotelRetrieval.getInstance();
//        String search = "ma";
//        try {
//            Hotel[] h1 = hr.getHotelByName(search);
//            Hotel[] h2 = hr.getHotelByName(search);
//            System.out.println("Equal: " + Arrays.toString(h1).equals(Arrays.toString(h2)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        System.out.println("-----------------------------------------------");
//        HotelRetrieval hr2 = HotelRetrieval.getInstance();
//        try {
//            Hotel[] h1 = hr2.getHotelByName(search);
//            Hotel[] h2 = hr2.getHotelByName(search);
//            System.out.println("Equal: " + Arrays.toString(h1).equals(Arrays.toString(h2)));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        System.out.println("HOTEL SEARCH");
        System.out.println("Info: enter 'exit' to leave");
        Scanner scanner = new Scanner(System.in);
        HotelRetrieval h = HotelRetrieval.getInstance();
        while (true) {
            System.out.println("-----------------------------------------");
            System.out.println("Search for hotel name: ");
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