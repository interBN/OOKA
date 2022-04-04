package A1;

public class Hotel {

    private final int id;
    private final String name;
    private final String city;

    public Hotel(String id, String name, String city) {
        this(Integer.parseInt(id), name, city);
    }

    public Hotel(int id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCity() {
        return city;
    }

    @Override
    public String toString() {
        return "Hotel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
