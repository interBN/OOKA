package A1.types;

import java.util.List;

@SuppressWarnings("unused")
public class Hotel {

    private final int id;
    private final String name;
    private final String city;

    public Hotel(List<String> list) {
        this(Integer.parseInt(list.get(0)), list.get(1), list.get(2));
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Hotel hotel = (Hotel) o;

        if (id != hotel.id) return false;
        if (name != null ? !name.equals(hotel.name) : hotel.name != null) return false;
        return city != null ? city.equals(hotel.city) : hotel.city == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
    }

    @Override
    public  String toString() {
            return "Hotel{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", city='" +city + '\'' +
                '}';
    }
}
