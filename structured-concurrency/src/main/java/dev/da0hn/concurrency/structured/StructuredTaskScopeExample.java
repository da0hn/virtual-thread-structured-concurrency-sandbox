package dev.da0hn.concurrency.structured;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("preview")
public class StructuredTaskScopeExample {

  public static void main(String[] args) {
    System.out.println("> Main thread started: " + Thread.currentThread().getName());

    // interruptMainThread();
    structuredTaskScopeExample();

    System.out.println("> Main thread ended: " + Thread.currentThread().getName());
  }

  private static void interruptMainThread() {
    final var mainThread = Thread.currentThread();
    Thread.ofPlatform().start(() -> {
      try {
        TimeUnit.SECONDS.sleep(2);
        mainThread.interrupt();
      }
      catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    });
  }

  private static void structuredTaskScopeExample() {
    final var expediaLongRunningTask = new LongRunningTask("expedia-task", 3, "100$", true);
    final var hotwireLongRunningTask = new LongRunningTask("hotwire-task", 10, "110$", true);

    try (var scope = new StructuredTaskScope<LongRunningTask.TaskResponse>()) {
      final var expediaSubtask = scope.fork(expediaLongRunningTask);
      final var hotwireSubtask = scope.fork(hotwireLongRunningTask);
      // scope.joinUntil(Instant.now().plusSeconds(2));
      scope.join();

      handleSubTask(expediaSubtask);
      handleSubTask(hotwireSubtask);
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private static void handleSubTask(StructuredTaskScope.Subtask<LongRunningTask.TaskResponse> subtask) {
    if (subtask.state() == State.FAILED) {
      System.out.println("Subtask failed: " + subtask.exception());
      return;
    }
    System.out.println("Subtask succeeded: " + subtask.get());
  }

}
