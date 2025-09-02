public  class Utils {
    public static Thread[] initThread(Runnable runnable,int n) {
        Thread[] threads = new Thread[n];
        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(runnable, "" + i);
        }
        return threads;
    }

    public static void startThreads(Thread[]threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    public static void joinThreads(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static double computeMean(long[] times) {
        long sum = 0;
        for (long time : times) {
            sum += time;
        }
        return (double) sum / times.length;
    }

    public static double computeVariance(long[] times, double mean) {
        double sumSquaredDiffs = 0.0;
        for (long time : times) {
            double diff = time - mean;
            sumSquaredDiffs += diff * diff;
        }
        return sumSquaredDiffs / times.length;
    }
}
