package parser;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * Created by roshanalwis on 9/26/17.
 */
public class ParallelTest {
    public static void main(String[] args) {
        int allRequestsCount = 20;
        int parallelism = 4; // Vary on your own

        ForkJoinPool forkJoinPool = new ForkJoinPool(parallelism);
        IntStream.range(0, parallelism).forEach(i -> forkJoinPool.submit(() -> {
            int chunkSize = allRequestsCount / parallelism;
            IntStream.range(i * chunkSize, i * chunkSize + chunkSize)
                    .forEach(num -> {

                        // Simulate long running operation
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        System.out.println(Thread.currentThread().getName() + ": " + num);
                    });
        }));
    }
}
