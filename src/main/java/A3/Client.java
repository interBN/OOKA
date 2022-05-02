package A3;

import java.io.IOException;
import java.nio.file.Path;

@SuppressWarnings("unused")
public class Client {

    public static void main(String[] args) {
        VersionControl v = new VersionControl("src/main/java/A3/saves", 3);
        Person p1 = new Person("Max", "Mustermann");
        System.out.println(p1);
        Path serialize = v.serialize(p1);
        try {
            v.deleteOldSaveFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Person p2 = (Person) v.deserialize(v.getLastSaveFile(), new Class[]{Person.class});
        System.out.println(p2);
    }

}
