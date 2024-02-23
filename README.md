# java-lock-bench

This project is to benchmark some concurrency control features of Java.

## What types of benchmarks?

The following types of benchmarks are included in the project.
- Counter
  - 100%: increment (read-modify-write) operation
  - These Java classes or features are used
    - java.util.concurrent.atomic.AtomicLong
    - java.util.concurrent.locks.ReentrantLock
    - java.util.concurrent.locks.ReentrantReadWriteLock
    - java.util.concurrent.Semaphore
    - java.util.concurrent.locks.StampedLock
    - `synchronized` keyword (intrinsic lock)
- Map
  - 20%: Read-modify-write operation
  - 80%: Read-only operation
  - These Java classes or features are used
    - java.util.concurrent.ConcurrentMap
    - java.util.concurrent.locks.ReentrantLock
    - java.util.concurrent.locks.ReentrantReadWriteLock
    - java.util.concurrent.Semaphore
    - java.util.concurrent.locks.StampedLock
      - java.util.concurrent.locks.StampedLock#writeLock() is used for read-only operation
      - java.util.concurrent.locks.StampedLock#tryOptimisticRead() is used for read-only operation
    - `synchronized` keyword (intrinsic lock)

## How to run the benchmark

Execute this command to run the Counter benchmark
```shell
./gradlew counter:jmh
```

Execute this command to run the Map benchmark
```shell
./gradlew map:jmh
```

Execute this command to run all the benchmarks
```shell
./gradlew jmh
```

