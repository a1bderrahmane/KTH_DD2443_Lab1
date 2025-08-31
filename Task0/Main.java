import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
public class Main {
    public static void main(String[] args) {
        Runnable0 runnable1 = new Runnable0();
        Thread[] threads =new Thread[8];
        for (int i = 0; i < 8; i++) {
            threads[i] = new Thread(runnable1, "" + i);
            threads[i].start();
        }
        for(Thread thread : threads)
        {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Goodbye");
    }
}
