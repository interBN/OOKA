package A1.interfaces;

import A1.types.Hotel;

public interface Port {

    Hotel[] getObjects(int type, String value) throws Exception;
}
