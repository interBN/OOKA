package A1.interfaces;

import A1.types.Hotel;

public interface HotelSearch {

    Hotel[] getHotelByName(String name) throws Exception;

}
