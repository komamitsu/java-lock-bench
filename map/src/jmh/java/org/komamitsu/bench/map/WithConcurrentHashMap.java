package org.komamitsu.bench.map;

import org.openjdk.jmh.annotations.*;

import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

import static org.komamitsu.bench.Constants.NUM_OF_OPS_PER_THREAD;
import static org.komamitsu.bench.Constants.NUM_OF_THREADS;
import static org.komamitsu.bench.map.Constants.NUM_OF_MAP_KEYS;
import static org.komamitsu.bench.map.Constants.NUM_WRITE_OP_OCCUR_WHEN_RANDOM_NUM_IS_MULTIPLE_OF;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithConcurrentHashMap {
    private final Random random = new Random();
    private final ConcurrentMap<Integer, Long> map = new ConcurrentHashMap<>();
    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("map.size: " + map.size());
    }

    private void increment() {
        int key = random.nextInt(NUM_OF_MAP_KEYS);
        while (true) {
            long value = map.computeIfAbsent(key, k -> 0L);
            if (map.replace(key, value, value + 1)) {
                return;
            }
        }
    }

    private Long read() {
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
