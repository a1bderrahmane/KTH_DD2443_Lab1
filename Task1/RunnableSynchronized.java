public class RunnableSynchronized implements Runnable {

    @Override
    public void run() {
        increment();
    }

    private synchronized void increment() {
        for (int i = 0; i < Main.NbIterations; i++) {
            Main.x++;
        }
    }

}
