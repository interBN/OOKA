package A2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Scanner;
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
            String[] options = {"show status", "load component", "unload component", "start component", "stop component", "serialize"};
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
                componentAssembler.unloadComponent(component);
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
                componentAssembler.serialize();
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
        int selectedClass = ask("Please select a class:", classes, BACK);
        if (selectedClass >= classes.length) return null;

        // SELECT METHOD
        String[] methods = componentAssembler.getMethods(classes[selectedClass], dirComponent);
        int selectedMethod = ask("Please select a method:", methods, BACK);
        if (selectedMethod >= methods.length) return null;

        // CREATE THREAD
        return new Component(pathResources, components[selectedComponent], classes[selectedClass], methods[selectedMethod], selectedMethod);
    }

    Component selectComponentToUnload() {
        String[] options = componentAssembler.listAllThreads("unload ");
        int unload = ask("Select to unload: ", options, NONE);
        if (unload >= options.length) {
            return null;
        }
        Component[] comps = componentAssembler.components.values().toArray(Component[]::new);
        return comps[unload];
    }

    Component selectComponentToStart() {
        String[] options = componentAssembler.listAllThreads("start ");
        int start = ask("Select to start: ", options, NONE);
        if (start >= options.length) {
            return null;
        }
        Component[] components = componentAssembler.components.values().toArray(Component[]::new);
        return components[start];
    }

    Component selectComponentToStop() {
        String[] options = componentAssembler.listAllThreads("stop ");
        int start = ask("Select to stop: ", options, NONE);
        if (start >= options.length) {
            return null;
        }
        Component[] components = componentAssembler.components.values().toArray(Component[]::new);
        return components[start];
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