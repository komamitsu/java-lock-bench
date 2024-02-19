package org.komamitsu.bench.map;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import static org.komamitsu.bench.Constants.NUM_OF_OPS_PER_THREAD;
import static org.komamitsu.bench.Constants.NUM_OF_THREADS;
import static org.komamitsu.bench.map.Constants.NUM_OF_MAP_KEYS;
import static org.komamitsu.bench.map.Constants.NUM_WRITE_OP_OCCUR_WHEN_RANDOM_NUM_IS_MULTIPLE_OF;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithSemaphore {
    private final Random random = new Random();
    private final Map<Integer, Long> map = new HashMap<>();
    private final Semaphore lock = new Semaphore(1);

    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("map.size: " + map.size());
    }

    private void increment() throws InterruptedException {
        int key = random.nextInt(NUM_OF_MAP_KEYS);
        lock.acquire();
        try {
            Long value = map.computeIfAbsent(key, k -> 0L);
            map.put(key, value + 1);
        }
        finally {
            lock.release();
        }
    }

    private Long read() throws InterruptedException {
        int key = random.nextInt(NUM_OF_MAP_KEYS);
        // StampedLock's read lock is too slow...
        lock.acquire();
        try {
            return map.get(key);
        }
        finally {
            lock.release();
        }
    }

    @Benchmark
    @Threads(NUM_OF_THREADS)
    public void run() throws InterruptedException {
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
