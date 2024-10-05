package dev.da0hn.concurrency.structured;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

@SuppressWarnings("preview")
public class ShutdownOnFailureExample {

  public static void main(String[] args) {
    System.out.println("> Main thread started: " + Thread.currentThread().getName());

    // interruptMainThread();
    structuredTaskScopeExample();

    System.out.println("> Main thread ended: " + Thread.currentThread().getName());
  }

  /**
   * Se pelo menos uma sub-tarefa falhar todas as sub-tarefas são canceladas.
   */
  private static void structuredTaskScopeExample() {
    final var expediaLongRunningTask = new LongRunningTask("fetch-data-database-subtask", 3, "row1", false);
    final var hotwireLongRunningTask = new LongRunningTask("fetch-data-api-subtask", 10, "some-json", true);

    try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
      final var expediaSubtask = scope.fork(expediaLongRunningTask);
      final var hotwireSubtask = scope.fork(hotwireLongRunningTask);
      scope.join();
      scope.throwIfFailed();

      System.out.println(expediaSubtask.get());
      System.out.println(hotwireSubtask.get());

    }
    catch (InterruptedException | ExecutionException e) {
      throw new RuntimeException(e);
    }
  }

}
