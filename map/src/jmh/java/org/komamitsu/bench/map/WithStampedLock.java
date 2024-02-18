package org.komamitsu.bench.map;

import org.openjdk.jmh.annotations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.StampedLock;

import static org.komamitsu.bench.Constants.*;
import static org.komamitsu.bench.map.Constants.*;

@State(Scope.Benchmark)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class WithStampedLock {
    private final Random random = new Random();
    private final Map<Integer, Long> map = new HashMap<>();
    private final StampedLock lock = new StampedLock();

    @TearDown(Level.Iteration)
    public void tearDown() {
        System.out.println("map.size: " + map.size());
    }

    private void increment() {
        int key = random.nextInt(NUM_OF_MAP_KEYS);
        long stamp = lock.writeLock();
        try {
            Long value = map.computeIfAbsent(key, k -> 0L);
            map.put(key, value + 1);
        }
        finally {
            lock.unlockWrite(stamp);
        }
    }

    private Long read() {
        int key = random.nextInt(NUM_OF_MAP_KEYS);
        // StampedLock's read lock is too slow...
        long stamp = lock.writeLock();
        try {
            return map.get(key);
        }
        finally {
            lock.unlockWrite(stamp);
        }
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
