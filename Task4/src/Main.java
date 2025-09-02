import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    static volatile int x = 0;

    static final CountingSemaphore sem1 = new CountingSemaphore(0);
    static final CountingSemaphore sem2 = new CountingSemaphore(0);
	static final CountingSemaphore sem3 = new CountingSemaphore(3);
    static final AtomicInteger nextId = new AtomicInteger(0);

    public static class Runner1 implements Runnable {
        private final int me;

        public Runner1() {
            this.me = nextId.getAndIncrement();
        }

        @Override
        public void run() {
            try {
                if (x == me) {
                    sem1.s_wait();
                    sem2.s_wait();
                } else {
                    sem2.s_wait();
                    sem1.s_wait();
                }

               
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } finally {
                sem2.signal();
                sem1.signal();
            }
        }
    }

    public static class Runner2 implements Runnable {
        @Override
        public void run() {
            try {
				int local=0;
				for(int i=0;i<100;i++)
				{
					local++;
				}
                sem3.s_wait();
                x+=local;
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            } finally {
                sem3.signal();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

		// Deadlock possibility
        // Thread t1 = new Thread(new Runner1());
        // Thread t2 = new Thread(new Runner1());
        // t1.start();
        // t2.start();
        // t1.join();
        // t2.join();


		Thread t3 = new Thread(new Runner2());
		Thread t4 = new Thread(new Runner2());
		Thread t5 = new Thread(new Runner2());
        t4.start();
        t5.start();
		t3.start();
        t4.join();
        t5.join();
		t3.join();
		System.out.println(x);

    }
}
