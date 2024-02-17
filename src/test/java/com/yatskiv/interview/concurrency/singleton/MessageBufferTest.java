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
public class MessageBufferTest {

  private static final int N_THREADS = 8;

  private final ExecutorService pool = Executors.newFixedThreadPool(N_THREADS);

  @Test
  void getInstance_makeParallelInstantiationOfSingleton_returnOneUniqueInstance()
      throws ExecutionException, InterruptedException {

    List<Future<MessageBuffer>> instances = new ArrayList<>();

    for (int i = 0; i < N_THREADS; i++) {
      instances.add(pool.submit(MessageBuffer::getInstance));
    }

    pool.shutdown();

    assertEquals(N_THREADS, instances.size(), "Expected capacity of the pool is " + N_THREADS);

    MessageBuffer instance = instances.getFirst().get();
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
  }

  @Test
  void write_successAddMessagesToTheBuffer_noErrors() {

    MessageBuffer buffer = fillBuffer();

    assertEquals(buffer.getMaxBufferSize(), buffer.getBufferSize());
  }

  @Test
  void read_successReadAllMessagesFromTheBuffer_noErrors() {

    MessageBuffer buffer = fillBuffer();

    for (int i = buffer.getBufferSize(); i > 0; i--) {
      System.out.println(buffer.read());
    }

    assertEquals(0, buffer.getBufferSize());
  }

  private MessageBuffer fillBuffer() {

    MessageBuffer buffer = MessageBuffer.getInstance();

    buffer.clear();

    for (int i = 0; i < buffer.getMaxBufferSize(); i++) {
      buffer.write("Message " + i);
    }

    return buffer;
  }
}
