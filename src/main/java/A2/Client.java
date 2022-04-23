package A2;

public class Client {

    public static void main(String[] args) {
        Runnable runnable = new ComponentAssembler();
        Thread thread = new Thread(runnable);
        thread.start();
    }
}
