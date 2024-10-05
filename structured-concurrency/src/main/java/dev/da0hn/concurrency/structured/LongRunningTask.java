package dev.da0hn.concurrency.structured;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class LongRunningTask implements Callable<LongRunningTask.TaskResponse> {

  private final String name;

  private final long time;

  private final String output;

  private final boolean fail;

  public LongRunningTask(String name, long time, String output, boolean fail) {
    this.name = name;
    this.time = time;
    this.output = output;
    this.fail = fail;
  }

  @Override
  public TaskResponse call() throws Exception {
    final var startTime = Instant.now();
    this.print("Task started");

    int numSecs = 0;
    while (numSecs++ < this.time) {
      if (Thread.interrupted()) {
        this.print("Task interrupted");
        throw new InterruptedException("Task " + this.name + " interrupted.");
      }
      print("Working... " + numSecs);
      try {
        TimeUnit.SECONDS.sleep(1);
      }
      catch (InterruptedException e) {
        this.print("Task interrupted");
        throw new InterruptedException("Task " + this.name + " interrupted.");
      }
    }

    if (this.fail) {
      this.print("Task failed");
      throw new RuntimeException("Task " + this.name + " failed.");
    }
    TimeUnit.SECONDS.sleep(this.time);
    this.print("Task ended");

    final var timeTaken = ChronoUnit.MILLIS.between(startTime, Instant.now());
    return new TaskResponse(this.name, this.output, timeTaken);
  }

  private void print(String message) {
    System.out.printf("> %s: %s%n", this.name, message);
  }

  public record TaskResponse(String name, String message, Long timeTaken) { }

}
