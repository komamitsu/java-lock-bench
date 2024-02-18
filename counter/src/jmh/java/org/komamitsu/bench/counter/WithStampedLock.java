package org.komamitsu.bench.counter;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.StampedLock;

import static org.komamitsu.bench.Constants.*;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithStampedLock {
    private volatile long counter;
    private final StampedLock lock = new StampedLock();

    @Setup(Level.Iteration)
    public void setup() {
        counter = 0;
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("counter: " + counter);
    }

    private void increment() {
        long stamp = lock.writeLock();
        try {
            counter++;
        }
        finally {
            lock.unlock(stamp);
        }
    }

    @Benchmark
    @Threads(NUM_OF_THREADS)
    public void run() {
        for (int i = 0; i < NUM_OF_OPS_PER_THREAD; i++) {
            increment();
        }
    }
}
