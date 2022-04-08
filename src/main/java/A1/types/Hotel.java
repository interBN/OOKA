package A1.types;

import java.util.List;
import java.util.Objects;

/**
 * @author Siu Cheng
 * @author Veronika Seidel
 */
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

    @SuppressWarnings("unused")
    public int getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public String getCity() {
        return city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Hotel hotel = (Hotel) o;
        if (id != hotel.id) return false;
        if (!Objects.equals(name, hotel.name)) return false;
        return Objects.equals(city, hotel.city);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        return result;
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
