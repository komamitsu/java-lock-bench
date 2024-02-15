package org.komamitsu.bench.counter;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static org.komamitsu.bench.counter.Constants.NUM_OF_OPS_PER_THREAD;
import static org.komamitsu.bench.counter.Constants.NUM_OF_THREADS;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithReentrantReadWriteLock {
    private volatile long counter;
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    @Setup(Level.Iteration)
    public void setup() {
        counter = 0;
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("counter: " + counter);
    }

    private void increment() {
        lock.writeLock().lock();
        try {
            counter++;
        }
        finally {
            lock.writeLock().lock();
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
