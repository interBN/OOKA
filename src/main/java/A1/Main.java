package A1;

import A1.types.Hotel;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        HotelRetrieval hr = HotelRetrieval.getInstance();
        String search = "ma";
        try {
            Hotel[] h1 = hr.getHotelByName(search);
            Hotel[] h2 = hr.getHotelByName(search);
            System.out.println("Equal: " + Arrays.toString(h1).equals(Arrays.toString(h2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("-----------------------------------------------");
        HotelRetrieval hr2 = HotelRetrieval.getInstance();
        try {
            Hotel[] h1 = hr2.getHotelByName(search);
            Hotel[] h2 = hr2.getHotelByName(search);
            System.out.println("Equal: " + Arrays.toString(h1).equals(Arrays.toString(h2)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}