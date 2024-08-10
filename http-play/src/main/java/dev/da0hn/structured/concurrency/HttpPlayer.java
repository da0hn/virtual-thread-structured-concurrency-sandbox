package dev.da0hn.structured.concurrency;

import java.util.concurrent.Executors;
import java.util.stream.IntStream;

public class HttpPlayer {

  private static final int NUMBER_OF_USERS = 2500;


  public static void main(String[] args) {
    final var vThreadFactory = Thread.ofVirtual().name("request-handler-", 0).factory();

    try (var executorService = Executors.newThreadPerTaskExecutor(vThreadFactory)) {
      executorService.submit(new UserRequestHandler());

      IntStream.rangeClosed(0, NUMBER_OF_USERS).forEach(i -> {
        executorService.submit(new UserRequestHandler());
      });
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }

  }

}
