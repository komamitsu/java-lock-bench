package org.komamitsu.bench.counter;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static org.komamitsu.bench.Constants.*;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithAtomicLong {
    private final AtomicLong counter = new AtomicLong();

    @Setup(Level.Iteration)
    public void setup() {
        counter.set(0);
    }

    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("counter: " + counter.get());
    }

    private void increment() {
        counter.incrementAndGet();
    }

    @Benchmark
    @Threads(NUM_OF_THREADS)
    public void run() {
        for (int i = 0; i < NUM_OF_OPS_PER_THREAD; i++) {
            increment();
        }
    }
}
