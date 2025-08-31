public class MainC {

	public static volatile int sharedInt = 0;
	public static volatile boolean done = false;
	final public static Object monitor = new Object();

	public static class Incrementer implements Runnable {

		public void run() {
			for (int i = 0; i < 1000000; i++) {
				sharedInt++;
			}
			// Incrementer has finished
			done = true;
			synchronized(monitor) {
				// wakes up the printer
				System.out.println("[Incrementer] I notify printer");
				monitor.notify();
			}
		}
	}

	public static class Printer implements Runnable {

		public void run() {
			// wait until incrementer notifies us
			synchronized(monitor) {
				try {
					while (!done) {
						System.out.println("[Printer] I go to sleep");
						monitor.wait();
						System.out.println("[Printer] I wake up");
					};
				} catch (InterruptedException e) {
					System.out.println("[Printer] I got interrupted");
					throw new RuntimeException();
				}
			}

			System.out.println("[Printer] sharedInt = " + sharedInt);
		}
	}

	public static void main(String [] args) {
		Thread incrementingThread = new Thread(new Incrementer());
		Thread printingThread = new Thread(new Printer());

		System.out.println("starting...\n");

		incrementingThread.start();
		printingThread.start();

		try {
			incrementingThread.join();
			printingThread.join();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
		System.out.println("\ndone");
	}
}
