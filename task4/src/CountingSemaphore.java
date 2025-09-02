class CountingSemaphore {
	private  int n;
	private  int signals;

	public CountingSemaphore(int n) {
		this.n = n;
		signals = 0;
	}

	public synchronized void s_wait() throws InterruptedException {
		n--;
		// Here making a loop that will constantly verify if the n condition is holding
		// to avoid spurious wakeup
		if (n<0)
		{
			while(signals==0)
			{
				wait();
			}
			signals--;
		}
	}

	public synchronized void signal() {
		n++;
		if (n <= 0) {
			notify();
			signals++;
		}
	}
}