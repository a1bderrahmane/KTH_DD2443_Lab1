import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static java.lang.Math.min;
import static java.lang.Math.max;

public class MainA {

    static final int n = 4;
    static final AtomicInteger nextId = new AtomicInteger(0);
    static volatile int[] counter = new int[n];
    static volatile int[] philosophers = new int[n];

    static Lock[] locks = new Lock[n];

    public static class Philosopher implements Runnable {
        private final int me;

        public Philosopher() {
            this.me = nextId.getAndIncrement();
        }

        @Override
        public void run() {
            while (true) {
                eat();
                think();
            }
        }

        private int left(int i) {
            return (i + 1) % n;
        }

        private int right(int i) {
            return (i - 1 + n) % n;
        }

        private void think() {

            locks[left(me)].unlock();
            locks[right(me)].unlock();
            philosophers[me] = 0;
            System.out.println("Now the philosopher " + me + " is thinking");

        }

        private void eat() {
            while (philosophers[right(me)] == 1 || philosophers[left(me)] == 1) {
                // System.out.println(" the philosopher " + me + " is busy-waiting");
            }
            while (counter[me] - getMinCounter() > 20) {
                // System.out.println(" the philosopher " + me + " is busy-waiting");

            }

            locks[min(right(me), left(me))].lock();
            locks[max(right(me), left(me))].lock();
            philosophers[me] = 1;
            counter[me]++;
            System.out.println("Now the philosopher " + me + " is eating");

        }
    }

    private static int getMinCounter() {
        int minimum = Integer.MAX_VALUE;
        for (int e : counter) {
            minimum = min(e, minimum);
        }
        return minimum;
    }

    public static void main(String[] args) throws InterruptedException {
        // initialize locks
        for (int i = 0; i < n; i++) {
            locks[i] = new ReentrantLock();
        }

        // At the beginning no philosopher is thinking nor eating
        for (int i = 0; i < n; i++) {
            counter[i] = i;
            philosophers[i] = 0;
        }

        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(new Philosopher());
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
    }
}
