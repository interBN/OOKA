package A2;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

import static A2.Helper.Breaker.EXIT;

public class CLI {

    ComponentAssembler componentAssembler;

    public CLI() {
        this.componentAssembler = ComponentAssembler.getInstance();
        Thread thread = new Thread(componentAssembler);
        thread.start();
    }

    void loop() {
        System.out.println("Start Component Assembler");
        while (true) {
            System.out.println(Helper.getLine());
            String[] options = {"show status", "load component", "unload component", "start component", "stop component"};
            int input = componentAssembler.ask("Please select:", options, EXIT);
            if (input == 0) { // show status
                componentAssembler.printStatus();
            } else if (input == 1) { // load component
                try {
                    componentAssembler.loadComponent();
                } catch (IOException | ClassNotFoundException | IllegalAccessException | InvocationTargetException |
                         InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 2) { // unload component
                componentAssembler.unloadComponent();
            } else if (input == 3) { // start component
                System.out.println("Start Component");
                try {
                    componentAssembler.startComponent();
                } catch (ClassNotFoundException | InvocationTargetException | IllegalAccessException |
                         NoSuchMethodException | MalformedURLException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            } else if (input == 4) { // stop component
                try {
                    componentAssembler.stopComponent();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else if (input >= options.length) { // exit
                System.out.println("Goodbye!");
                break;
            }
        }
    }
}