public class Main {

	public static Buffer buffer;

	public static class Producer implements Runnable {
		public void run() {
			try {
				for (int i = 0; i < 1000000; i++) {
					buffer.add(i);
				}
				buffer.close();
			} catch (BufferClosedException e) {
				throw new Error("Buffer closed before producerThread ends");
			}
		}
	}

	public static class Consumer implements Runnable {
		public void run() {
			try {
				while (true) {
					int i = buffer.remove();
					System.out.print(i + ", ");
				}
			} catch (BufferClosedException e) {
				return;
			}
		}
	}

	public static void main(String [] args) {
		buffer = new Buffer(100);

		Thread producerThread = new Thread(new Producer(), "producer");
		Thread consumerThread = new Thread(new Consumer(), "consumer");

		producerThread.start();
		consumerThread.start();
		try {
			producerThread.join();
			consumerThread.join();
		} catch (InterruptedException e) {
			throw new Error ("Thread interrupted : " + e.getMessage());
		}
	}
}
