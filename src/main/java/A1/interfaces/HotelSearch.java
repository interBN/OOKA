package A1.interfaces;

import A1.types.Hotel;

/**
 * @author Siu Cheng
 * @author Veronika Seidel
 */
public interface HotelSearch {

    Hotel[] getHotelByName(String name) throws Exception;

}
