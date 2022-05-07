package A3;

public interface PersonInterface {
    @Override
    String toString();

    String getFirstname();

    void setFirstname(String firstname);

    String getLastname();

    void setLastname(String lastname);

    void log(String msg);
}
