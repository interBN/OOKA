package A2;

public class Client {

    public static void main(String[] args) {
        ComponentAssembler runnable = new ComponentAssembler();
        Thread thread = new Thread(runnable);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        runnable.close();
        thread.interrupt();
    }
}
