package org.komamitsu.bench.counter;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.komamitsu.bench.Constants.NUM_OF_OPS_PER_THREAD;
import static org.komamitsu.bench.Constants.NUM_OF_THREADS;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithSemaphore {
    private volatile long counter;
    private final Semaphore lock = new Semaphore(1);

    @Setup(Level.Iteration)
    public void setup() {
        counter = 0;
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("counter: " + counter);
    }

    private void increment() throws InterruptedException {
        lock.acquire();
        try {
            counter++;
        }
        finally {
            lock.release();
        }
    }

    @Benchmark
    @Threads(NUM_OF_THREADS)
    public void run() throws InterruptedException {
        for (int i = 0; i < NUM_OF_OPS_PER_THREAD; i++) {
            increment();
        }
    }
}
