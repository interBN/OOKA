package A1.interfaces;

import A1.types.Hotel;

public interface DataProvider {

    Hotel[] getObjects(int type, String value) throws Exception;
}
