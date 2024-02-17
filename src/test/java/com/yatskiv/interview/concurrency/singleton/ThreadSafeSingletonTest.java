package com.yatskiv.interview.concurrency.singleton;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class ThreadSafeSingletonTest {

  private static final int N_THREADS = 8;

  private final ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);

  @Test
  void getInstance_makeParallelInstantiationOfSingleton_returnOneUniqueThreadSafeSingletonInstance()
      throws ExecutionException, InterruptedException {

    List<Future<ThreadSafeSingleton>> instances = new ArrayList<>();

    for (int i = 0; i < N_THREADS; i++) {
      instances.add(pool.submit(ThreadSafeSingleton::getInstance));
    }

    pool.shutdown();

    assertEquals(N_THREADS, instances.size(), "Expected capacity of the pool is " + N_THREADS);

    ThreadSafeSingleton instance = instances.getFirst().get();
    assertTrue(
        instances.stream()
            .allMatch(
                i -> {
                  try {
                    return i.get().equals(instance);
                  } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                  }
                }),
        "Thread-safe singleton instances must all be equals");

    instance.printInstanceInfo();
  }
}
