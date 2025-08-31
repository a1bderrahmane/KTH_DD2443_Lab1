import java.util.Arrays;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;

public class Main {
    public volatile static int x;
    public final static int NbIterations = 10_000_000;

    public static void main(String[] args) {
        // getProgramPerformance(4,5,30);
        int[] X_values = { 3, 5, 10, 15, 20, 30, 40, 50, 60 }; // try different X
        int[] Y_values = { 3, 5, 10, 15, 20, 30, 40, 50, 60 }; // try different Y
        int n = 4;
        // runMultipleExperiments(n, X_values, Y_values, "performance_stats.csv");
        getThreadsStats("Threads_stats.csv");
    }

    static Thread[] initThread(Runnable runnable, int n) {
        Thread[] threads = new Thread[n];

        for (int i = 0; i < n; i++) {
            threads[i] = new Thread(runnable, "" + i);
        }
        return threads;
    }

    static void startThreads(Thread[] threads) {
        for (Thread thread : threads) {
            thread.start();
        }
    }

    static void joinThreads(Thread[] threads) {
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    static double computeMean(long[] times) {
        long sum = 0;
        for (long time : times) {
            sum += time;
        }
        return (double) sum / times.length;
    }

    static double computeVariance(long[] times, double mean) {
        double sumSquaredDiffs = 0.0;
        for (long time : times) {
            double diff = time - mean;
            sumSquaredDiffs += diff * diff;
        }
        return sumSquaredDiffs / times.length;
    }

    static void task1A(int n) {
        Runnable1 runnable = new Runnable1();

        Thread[] threads = initThread(runnable, n);
        startThreads(threads);
        joinThreads(threads);
        System.out.println(x);
    }

    static void task1B(int n) {
        RunnableSynchronized runnable = new RunnableSynchronized();

        Thread[] threads = initThread(runnable, n);
        startThreads(threads);
        joinThreads(threads);
    }

    static long run_experiment(int n) {
        RunnableSynchronized runnable = new RunnableSynchronized();
        Thread[] threads = initThread(runnable, n);

        long startTime = System.nanoTime();
        startThreads(threads);
        joinThreads(threads);
        long endTime = System.nanoTime();

        System.out.println("The program took :" + (endTime - startTime));
        return (endTime - startTime);
    }

    static void getProgramPerformance(int n, int X, int Y) {
        long[] elapsed_X = new long[X];
        long[] elapsed_Y = new long[Y];

        for (int i = 0; i < X; i++) {
            elapsed_X[i] = run_experiment(n);
        }
        for (int i = 0; i < Y; i++) {
            elapsed_Y[i] = run_experiment(n);
            ;
        }
        System.out.println("X iterations :" + Arrays.toString(elapsed_X));
        System.out.println("Y iterations :" + Arrays.toString(elapsed_Y));

    }

    static void runMultipleExperiments(int n, int[] X_values, int[] Y_values, String filePath) {
        StringBuilder sb = new StringBuilder();
        sb.append("X,Y,countX,countY,meanX,varianceX,meanY,varianceY\n");

        for (int X : X_values) {
            for (int Y : Y_values) {
                long[] elapsed_X = new long[X];
                long[] elapsed_Y = new long[Y];

                for (int i = 0; i < X; i++)
                    elapsed_X[i] = run_experiment(n);
                for (int i = 0; i < Y; i++)
                    elapsed_Y[i] = run_experiment(n);

                double meanX = computeMean(elapsed_X);
                double varX = computeVariance(elapsed_X, meanX);
                double meanY = computeMean(elapsed_Y);
                double varY = computeVariance(elapsed_Y, meanY);

                sb.append(X).append(",").append(Y).append(",")
                        .append(elapsed_X.length).append(",").append(elapsed_Y.length).append(",")
                        .append(meanX).append(",").append(varX).append(",")
                        .append(meanY).append(",").append(varY).append("\n");

                System.out.println("Finished run with X=" + X + " Y=" + Y);
            }
        }

        try {
            Files.writeString(Paths.get(filePath), sb.toString(), StandardCharsets.UTF_8);
            System.out.println("Saved all results to " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write CSV: " + e.getMessage(), e);
        }
    }

    static void getThreadsStats(String filePath) {
        final int X = 3, Y = 30;
        StringBuilder sb = new StringBuilder();
        sb.append("n,X,Y,countX,countY,meanX,varianceX,meanY,varianceY\n");

        for (int n = 1; n <= 64; n++) {
            long[] elapsed_X = new long[X];
            long[] elapsed_Y = new long[Y];

            // collect X runs
            for (int i = 0; i < X; i++) {
                elapsed_X[i] = run_experiment(n);
            }
            // collect Y runs
            for (int i = 0; i < Y; i++) {
                elapsed_Y[i] = run_experiment(n);
            }

            double meanX = computeMean(elapsed_X);
            double varX = computeVariance(elapsed_X, meanX);
            double meanY = computeMean(elapsed_Y);
            double varY = computeVariance(elapsed_Y, meanY);

            sb.append(n).append(",")
                    .append(X).append(",").append(Y).append(",")
                    .append(elapsed_X.length).append(",").append(elapsed_Y.length).append(",")
                    .append(meanX).append(",").append(varX).append(",")
                    .append(meanY).append(",").append(varY).append("\n");

            System.out.println("Finished n=" + n);
        }

        try {
            Files.writeString(Paths.get(filePath), sb.toString(), StandardCharsets.UTF_8);
            System.out.println("Saved thread stats to " + filePath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to write CSV: " + e.getMessage(), e);
        }
    }

}