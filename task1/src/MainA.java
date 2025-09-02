public class MainA {
    public volatile static int x;
    public final static int NbIterations = 10_000_000;

    public static class Runnable1 implements Runnable {

        @Override
        public void run() {
            increment();
        }

        private void increment() {
            for (int i = 0; i < MainA.NbIterations; i++) {
                MainA.x++;
            }
        }
    }
    public static void main(String[] args) {
        int n = 4;
        Runnable1 runnable = new Runnable1(); // define Runnable1 elsewhere
        Thread[] threads = Utils.initThread(runnable, n);
        Utils.startThreads(threads);
        Utils.joinThreads(threads);
        System.out.println(x);
    }

}
