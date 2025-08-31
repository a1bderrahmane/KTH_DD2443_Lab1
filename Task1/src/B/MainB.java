package B;
import Utils.Utils;
public class MainB {
    public volatile static int x;
    public final static int NbIterations = 10_000_000;

    public static class RunnableSynchronized implements Runnable {
    @Override
    public void run() {
        increment();
    }
    private  void increment() {
        int local=0;
        for (int i = 0; i < MainB.NbIterations; i++) {
            local++;
        }
        synchronized (MainB.class) {
            MainB.x += local;
        }
    }

}
    public static void main(String[] args) {
        int n = 4;
        RunnableSynchronized runnable = new RunnableSynchronized();

        Thread[] threads =Utils.initThread(runnable,n);
        Utils.startThreads(threads);
        Utils.joinThreads(threads);
        System.out.println(x);
    }

}
