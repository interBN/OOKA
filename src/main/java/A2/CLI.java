package A2;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static A2.Helper.*;
import static A2.Helper.Breaker.*;

public class CLI {

    @SuppressWarnings("FieldCanBeLocal")
    private final String pathResources = "src/main/java/A2/resources/";
    ComponentAssembler componentAssembler;
    Scanner scanner;


    public CLI() {
        this.componentAssembler = ComponentAssembler.getInstance();
        Thread thread = new Thread(componentAssembler);
        thread.start();
        this.scanner = new Scanner(System.in);
    }

    void loop() {
        System.out.println("Start Component Assembler CLI");
        while (true) {
            System.out.println(Helper.getLine());
            String[] options = {"show status", "load component", "unload component", "start component", "stop component", "serialize", "deserialize"};
            int input = ask("Please select:", options, EXIT);
            if (input == 0) { // show status
                componentAssembler.printStatus();
            } else if (input == 1) { // load component
                try {
                    Component component = selectComponent();
                    if (component == null) {
                        continue;
                    }
                    componentAssembler.loadComponent(component);
                } catch (IOException | ClassNotFoundException | InterruptedException | InvocationTargetException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 2) { // unload component
                Component component = selectComponentToUnload();
                if (component == null) {
                    continue;
                }
                try {
                    componentAssembler.unloadComponent(component);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 3) { // start component
                System.out.println("Start Component");
                try {
                    Component component = selectComponentToStart();
                    if (component == null) {
                        continue;
                    }
                    componentAssembler.startComponent(component);
                } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                         NoSuchMethodException | MalformedURLException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 4) { // stop component
                try {
                    Component component = selectComponentToStop();
                    if (component == null) {
                        continue;
                    }
                    componentAssembler.stopComponent(component);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 5) { // serialize component assembler
                componentAssembler.serializeComponents();
            } else if (input == 6) { // deserialize component assembler
                try {
                    selectComponentToDeserialize();
                } catch (IOException | ClassNotFoundException | InterruptedException | InvocationTargetException |
                         IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else if (input >= options.length) { // exit
                System.out.println("Goodbye!");
                break;
            }
        }
    }

    Component selectComponent() throws IOException, ClassNotFoundException {
        // SELECT COMPONENT
        String[] components = componentAssembler.getJarFiles(pathResources);
        int selectedComponent = ask("Please select a component:", components, BACK);
        if (selectedComponent >= components.length) return null;

        // SELECT CLASS
        String dirComponent = pathResources + components[selectedComponent];
        String[] classes = componentAssembler.getClassNamesFromJarFile(dirComponent);
        int selectedClass = 0;
        if (classes.length > 1) {
            selectedClass = ask("Please select a class:", classes, BACK);
            if (selectedClass >= classes.length) return null;
        }

        // SELECT CONSTRUCTOR
        Constructor<?>[] cons = componentAssembler.getConstructors(classes[selectedClass], dirComponent);
        List<String> conNames = Arrays.stream(cons).map(Constructor::getName).collect(Collectors.toList());

        Optional<Constructor<?>> first = Arrays.stream(cons).findFirst();
        Constructor<?> constructor = first.get();

        int selectedCon = 0;
        if (cons.length > 1) {
            selectedCon = ask("Please select a method:", conNames.toArray(String[]::new), BACK);
            if (selectedCon >= cons.length) return null;
        }

        // CREATE THREAD
        return new Component(pathResources, components[selectedComponent], classes[selectedClass], constructor);
    }

    Component selectComponentToUnload() {
        String[] options = componentAssembler.listAllThreads("unload ");
        int select = ask("Select to unload: ", options, BACK);
        if (select >= options.length) {
            return null;
        }
        Component[] comps = componentAssembler.components.values().toArray(Component[]::new);
        return comps[select];
    }

    Component selectComponentToStart() {
        String[] options = componentAssembler.listAllThreads("start ");
        int select = ask("Select to start: ", options, BACK);
        if (select >= options.length) {
            return null;
        }
        Component[] components = componentAssembler.components.values().toArray(Component[]::new);
        return components[select];
    }

    Component selectComponentToStop() {
        String[] options = componentAssembler.listAllThreads("stop ");
        int select = ask("Select to stop: ", options, BACK);
        if (select >= options.length) {
            return null;
        }
        Component[] components = componentAssembler.components.values().toArray(Component[]::new);
        return components[select];
    }

    void selectComponentToDeserialize() throws IOException, ClassNotFoundException, InterruptedException, InvocationTargetException, IllegalAccessException {
        Set<Path> pathSet = componentAssembler.versionControl.listFiles();
        if (pathSet.size() == 0) {
            System.out.println("No save files to load.");
            return;
        }
        String[] options = new String[pathSet.size()];
        int counter = 0;
        for (Path path : pathSet) {
            options[counter++] = counter == 1 ? path.toString() + YELLOW + " < latest" + ANSI_RESET : path.toString();
        }
        int select = ask("Please select save file to load", options, BACK);
        if (select >= options.length) {
            return;
        }
        Path[] pathArray = pathSet.toArray(new Path[0]);
        componentAssembler.deserializeComponents(pathArray[select]);
    }

    boolean askYesNo(String question) {
        return ask(question, new String[]{"yes", "no"}, NONE) == 0;
    }

    int ask(String question, String[] options, Helper.Breaker breaker) {
        System.out.println(GREEN + question + ANSI_RESET);
        IntStream.range(0, options.length).mapToObj(i -> "[" + i + "] " + options[i]).forEach(System.out::println);
        if (breaker == BACK || breaker == EXIT) {
            System.out.println("[" + options.length + "] " + breaker.toString().toLowerCase(Locale.ROOT));
        }
        int input = scanner.nextInt();
        if (input < 0 || (breaker == NONE && input >= options.length) || input >= options.length + 1) {
            System.out.println(RED + "Wrong input." + ANSI_RESET);
            return ask(question, options, breaker);
        }
        return input;
    }
}