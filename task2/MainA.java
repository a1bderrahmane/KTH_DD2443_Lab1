public class MainA {

	public static volatile int sharedInt = 0;

	public static class Incrementer implements Runnable {
		public void run() {
			for (int i = 0; i < 1000000; i++) {
				sharedInt++;
			}
		}
	}

	public static class Printer implements Runnable {
		public void run() {
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
