package org.komamitsu.bench.simple;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.*;

import static org.komamitsu.bench.simple.Constants.NUM_OF_OPS_PER_THREAD;
import static org.komamitsu.bench.simple.Constants.NUM_OF_THREADS;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithSynchronized {
    private long counter;

    @Setup(Level.Iteration)
    public void setup() {
        counter = 0;
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("counter: " + counter);
    }

    private synchronized void increment() {
        counter++;
    }

    @Benchmark
    @Threads(NUM_OF_THREADS)
    public void run() {
        for (int i = 0; i < NUM_OF_OPS_PER_THREAD; i++) {
            increment();
        }
    }
}
