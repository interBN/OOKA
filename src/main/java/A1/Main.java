package A1;

import A1.types.Hotel;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        HotelRetrieval hr = new HotelRetrieval();
        String search = "Jahres";
        try {
            Hotel[] h1 = hr.getHotelByName(search);
            System.out.println(Arrays.toString(h1));
            Hotel[] h2 = hr.getHotelByName(search);
            System.out.println(Arrays.toString(h2));
            System.out.println(h1[0].equals(h2[0]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
