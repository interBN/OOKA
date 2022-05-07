package A3;

import A2.ComponentAssembler;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

@SuppressWarnings({"DuplicatedCode", "unused", "FieldCanBeLocal"})
public class VersionControl {

    private final String dir;
    private final String filename;
    private final String type = ".xml";
    private final int keepFiles;


    public VersionControl(String dir, int keepFiles) {
        this(dir, "save_", keepFiles);
    }

    public VersionControl(String dir, String filenamePrefix, int keepFiles) {
        this.dir = dir;
        this.filename = filenamePrefix;
        this.keepFiles = keepFiles;
    }

    /**
     * source: <a href="https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream">https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream</a>
     */
    public Path serialize(Object o, Class<?>[] classes) {
        XStream xstream = new XStream(new DomDriver());
        xstream.allowTypes(classes);
        String xml = xstream.toXML(o);
        FileOutputStream fos = null;
        try {
            Files.createDirectories(Paths.get(dir));
            Path path = Paths.get(dir, filename + new Date().getTime() + type);
            fos = new FileOutputStream(path.toString());
            fos.write("<?xml version=\"1.0\"?>".getBytes(StandardCharsets.UTF_8));
            byte[] bytes = xml.getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);
            return path;
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
        return null;
    }

    /**
     * source: <a href="https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream">https://stackoverflow.com/questions/13063815/save-xml-file-with-xstream</a>
     */
    public Object deserialize(Path path, Class<?>[] classes) {
        XStream xstream = new XStream(new DomDriver());
        xstream.allowTypes(classes);
        Object o = ComponentAssembler.getInstance(); // TODO: Exception instead?
        try {
            File xmlFile = new File(path.toString());
            o = xstream.fromXML(xmlFile);
        } catch (Exception e) {
            System.err.println("Error in XML Read: " + e.getMessage());
        }
        return o;
    }

    /**
     * if: keepFiles = 1
     * and: dir has 3 files: [10, 20, 30]
     * then: delete files: [10, 20]
     */
    public Path[] deleteOldSaveFiles() throws IOException {
        Set<Path> files = listFiles();
        if (files.size() > keepFiles) {
            Path[] delete = Arrays.copyOfRange(files.toArray(Path[]::new), 0, files.size() - keepFiles);
            deleteFiles(delete);
            return delete;
        }
        return new Path[]{};
    }


    /**
     * source: <a href="https://www.baeldung.com/java-list-directory-files">https://www.baeldung.com/java-list-directory-files</a>
     * if dir has 3 files: [10, 20, 30]
     *
     * @return [10, 20, 30]
     */
    public Set<Path> listFiles() throws IOException {
        Set<Path> files = new HashSet<>();
        Files.walkFileTree(Paths.get(dir), new SimpleFileVisitor<>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                if (!Files.isDirectory(file)) {
                    files.add(file);
                }
                return FileVisitResult.CONTINUE;
            }
        });
        return files;
    }

    /**
     * if dir has 3 files: [10, 20, 30]
     *
     * @return 30
     */
    public Path getLastSaveFile() {
        try {
            return new TreeSet<>(listFiles()).last();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteFiles(Path[] files) {
        Arrays.stream(files).forEach(this::deleteFile);
    }

    public void deleteFile(Path file) {
        File myObj = new File(file.toString());
        System.out.println(myObj.delete() ? "Deleted the file: " + myObj.getName() : "Failed to delete the file.");
    }
}