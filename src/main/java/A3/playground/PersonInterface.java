package A3.playground;

public interface PersonInterface {
    @Override
    String toString();

    String getFirstname();

    void setFirstname(String firstname);

    String getLastname();

    void setLastname(String lastname);

    void log(String msg);
}
