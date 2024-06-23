package dev.da0hn.structured.concurrency.threads.virtual;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ProjectLoomEx1 {

  public static void main(String[] args) {
    System.out.println("Starting main " + Thread.currentThread());

    final var vThreadFactory = Thread.ofVirtual().name("vThread-", 0).factory();

    try (var executorService = Executors.newThreadPerTaskExecutor(vThreadFactory)) {
      for (int i = 0; i < 100_000; i++) {
        executorService.submit(ProjectLoomEx1::handleUserRequest);
      }
    }

    System.out.println("Ending main " + Thread.currentThread());
  }

  private static void handleUserRequest() {
    System.out.println("Starting thread " + Thread.currentThread());

    try {
      TimeUnit.SECONDS.sleep(2);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }

    System.out.println("Ending thread " + Thread.currentThread());
  }

}
