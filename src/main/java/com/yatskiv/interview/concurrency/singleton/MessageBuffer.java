package com.yatskiv.interview.concurrency.singleton;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageBuffer {

  private static final int BUFFER_SIZE = 10;

  @Getter private static final MessageBuffer instance;

  private final List<String> buffer = new ArrayList<>(BUFFER_SIZE);

  private int cursor = 0;

  static {
    instance = new MessageBuffer();
  }

  private MessageBuffer() {

    log.info("Instance initialized");
  }

  public int getMaxBufferSize() {

    return BUFFER_SIZE;
  }

  public int getBufferSize() {

    return cursor;
  }

  public void clear() {

    buffer.clear();
    cursor = 0;
  }

  public void write(String message) {

    log.info("Add message '{}' at {}", message, cursor);

    buffer.add(cursor++, message);
  }

  public String read() {

    final String message = buffer.get(--cursor);

    log.info("Get message '{}' from {}", message, cursor);

    return message;
  }
}
