package org.komamitsu.bench.map;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static org.komamitsu.bench.Constants.NUM_OF_OPS_PER_THREAD;
import static org.komamitsu.bench.Constants.NUM_OF_THREADS;
import static org.komamitsu.bench.map.Constants.NUM_OF_MAP_KEYS;
import static org.komamitsu.bench.map.Constants.NUM_WRITE_OP_OCCUR_WHEN_RANDOM_NUM_IS_MULTIPLE_OF;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithSynchronized {
    private final Random random = new Random();
    private final Map<Integer, Long> map = new HashMap<>();
    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("map.size: " + map.size());
    }

    private synchronized void increment() {
        int key = random.nextInt(NUM_OF_MAP_KEYS);
        Long value = map.computeIfAbsent(key, k -> 0L);
        map.put(key, value + 1);
    }

    private synchronized long read() {
        int key = random.nextInt(NUM_OF_MAP_KEYS);
        return map.get(key);
    }

    @Benchmark
    @Threads(NUM_OF_THREADS)
    public void run() {
        for (int i = 0; i < NUM_OF_OPS_PER_THREAD; i++) {
            if (i % NUM_WRITE_OP_OCCUR_WHEN_RANDOM_NUM_IS_MULTIPLE_OF == 0) {
                increment();
            }
            else {
                read();
            }
        }
    }
}
