public class MainB {

	public static volatile int sharedInt = 0;
	public static volatile boolean done = false;

	public static class Incrementer implements Runnable {
		public void run() {
			for (int i = 0; i < 1000000; i++) {
				sharedInt++;
			}
			// Incrementer has finished
			done = true;
		}
	}

	public static class Printer implements Runnable {
		public void run() {
			// wait until incrementer is done
			while (!done) {};

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
