package com.yatskiv.interview.concurrency.singleton;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ThreadSafeSingleton {

  @Getter private static final ThreadSafeSingleton instance;

  static {
    instance = new ThreadSafeSingleton();
  }

  private ThreadSafeSingleton() {

    log.info("Instance initialized");
  }

  public void printInstanceInfo() {

    log.info("Instance info: {}", instance);
  }
}
