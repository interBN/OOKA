package A3;

import A2.ComponentAssembler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

@SuppressWarnings("DuplicatedCode")
public class XStreamTest {

    static Path path = Paths.get("src/main/java/A3/saves", "myfilename");

    public static void main(String[] args) {
        Person p1 = new Person("Max", "Mustermann");
        serialize(p1);
        Person p2 = (Person) deserialize();
        System.out.println(p2.toString());
    }

    /**
     * source: <a href="https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream">https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream</a>
     */
    public static void serialize(Object o) {
        Class<?>[] classes = new Class[]{Person.class};
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);

        String xml = xstream.toXML(o);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(path.toString());
            fos.write("<?xml version=\"1.0\"?>".getBytes(StandardCharsets.UTF_8)); //write XML header, as XStream doesn't do that for us
            byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * source: <a href="https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream">https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream</a>
     */
    public static Object deserialize() {
        Class<?>[] classes = new Class[]{Person.class};
        XStream xstream = new XStream(new DomDriver());
        XStream.setupDefaultSecurity(xstream);
        xstream.allowTypes(classes);
        Object o = ComponentAssembler.getInstance(); //if there is an error during deserialization, this is going to be returned, is this what you want?
        try {
            File xmlFile = new File(path.toString());
            o = xstream.fromXML(xmlFile);
        } catch (Exception e) {
            System.err.println("Error in XML Read: " + e.getMessage());
        }
        return o;
    }

    static class Person {
        String firstname;
        String lastname;

        public Person(String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
        }

        @Override
        public String toString() {
            return "Person{" +
                    "firstname='" + firstname + '\'' +
                    ", lastname='" + lastname + '\'' +
                    '}';
        }
    }

}
