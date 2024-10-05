package dev.da0hn.concurrency.structured;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

  public static void main(String[] args) {
    final var longRunningTask = new LongRunningTask(
        "LongRunningTask-1",
        10,
        "Task completed successfully",
        false
    );
    System.out.println("> Main thread started: " + Thread.currentThread().getName());

    try (final var executorService = Executors.newFixedThreadPool(2)) {
      final var futureTaskResponse = executorService.submit(longRunningTask);

      TimeUnit.SECONDS.sleep(3);
      futureTaskResponse.cancel(true);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("> Main thread ended: " + Thread.currentThread().getName());
  }

}
