import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {

	private int[] buffer;
	private int bufferSize;

	private int producterIndex = 0;
	private int consumerIndex = 0;
	private boolean bufferEmpty = true;
	private boolean bufferClosed = false;

	final Lock lock = new ReentrantLock();
	final Condition notEmpty = lock.newCondition();
	final Condition notFull = lock.newCondition();

	public Buffer(int size) {
		bufferSize = size;
		buffer = new int[size];
	}

	public void add(int i) throws BufferClosedException {
		lock.lock();
		try {
			// exception case
			if (bufferClosed) {
				throw new BufferClosedException();
			}

			// we wait until there is space in the buffer
			while (!bufferEmpty && producterIndex == consumerIndex) {
				notFull.await();
			}

			// we write into the buffer
			buffer[producterIndex] = i;
			producterIndex = (producterIndex + 1) % bufferSize;

			// if buffer was empty before we wrote on it, notify a consumer
			if (bufferEmpty) {
				bufferEmpty = false;
				notEmpty.signal();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public int remove() throws BufferClosedException {
		lock.lock();
		try {
			// exception case
			if (bufferClosed && bufferEmpty) {
				throw new BufferClosedException();
			}

			// we wait until there is something to consume in the buffer
			while (bufferEmpty) {
				notEmpty.await();
			}

			// we consume next value
			int val = buffer[consumerIndex];
			boolean wasBufferFull = consumerIndex == producterIndex;
			consumerIndex = (consumerIndex + 1) % bufferSize;

			// if buffer is now empty, update emptyBuffer value
			bufferEmpty = consumerIndex == producterIndex;

			// if buffer was full before we took a value on it, notify a producer
			if (wasBufferFull) {
				notFull.signal();
			}
			return val;
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} finally {
			lock.unlock();
		}
	}

	public void close() {
		// a closed buffer is represented with producerIndex < 0
		lock.lock();
		try  {
			bufferClosed = true;
		} finally {
			lock.unlock();
		}
	}
}
