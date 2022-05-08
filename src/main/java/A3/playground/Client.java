package A3.playground;

import A3.VersionControl;

import java.io.IOException;
import java.nio.file.Path;

@SuppressWarnings("unused")
public class Client {

    public static void main(String[] args) {
        VersionControl v = new VersionControl("src/main/java/A3/saves", 3);
        Person p1 = new Person("Max", "Mustermann");
        System.out.println(p1);
        Class<?>[] classes = new Class[]{ Client.class};
        Path serialize = v.serialize(p1, classes);
        try {
            Path[] deletedFiles = v.deleteOldSaveFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Person p2 = (Person) v.deserialize(v.getLastSaveFile(), new Class[]{Person.class});
        System.out.println(p2);
    }

}
