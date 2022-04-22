package A2;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    static Object neuesExemplar(String pfad, String klassename) throws Exception {
        URL url = new File(pfad).toURL();
        URLClassLoader cl = new URLClassLoader(new URL[]{url});
        Class c = cl.loadClass(klassename);
        // Method[] methods = c.getMethods();

        Method method = c.getDeclaredMethod("main", String[].class);

        String[] args = {"arg1", "arg2"};
        System.out.println("A");

        Object instance = c.newInstance();
        System.out.println("B");
        Object result = method.invoke(null, (Object) args);

        return c.newInstance();
    }

    public static void main(String[] args) throws Exception {

        String path = "src/main/java/A2/resources/A1.jar";

        String pathResources = "src/main/java/A2/resources/";

        System.out.println("Start Component Assembler");


        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("[" + "0" + "] status");
            System.out.println("[" + "1" + "] run component");
            System.out.println("[" + "2" + "] stop component");

            String input = scanner.next();
            input = input.toLowerCase();


            if (input.equals("exit")) {
                break;
            } else if (input.equals("1")) {
                loadJar(pathResources);
            }
        }
    }

    private static void loadJar(String pathResources) {
        Set<String> bla = listFilesUsingJavaIO(pathResources);
        System.out.println("Please select a component by number:");
        Object[] namesArray = bla.toArray();
        for (int i = 0; i < namesArray.length; i++) {
            System.out.println("[" + i + "] " + namesArray[i]);
        }
        Scanner scanner = new Scanner(System.in);
        String input = scanner.next();


        // Object a1 = neuesExemplar(path, "A1.ConsoleTester");

    }

    public static Set<String> listFilesUsingJavaIO(String dir) {
        return Stream.of(Objects.requireNonNull(new File(dir).listFiles()))
                .filter(file -> !file.isDirectory() && file.getName().endsWith(".jar"))
                .map(File::getName)
                .collect(Collectors.toSet());
    }

}
