package dev.da0hn.concurrency.structured;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;

@SuppressWarnings("preview")
public class ShutdownOnSuccessExample {

  public static void main(String[] args) {
    System.out.println("> Main thread started: " + Thread.currentThread().getName());

    // interruptMainThread();
    structuredTaskScopeExample();

    System.out.println("> Main thread ended: " + Thread.currentThread().getName());
  }

  /**
   * Se pelo menos uma sub-tarefa finalizar com sucesso todas as sub-tarefas pendentes são canceladafinalizar com sucesso todas as sub-tarefas pendentes são canceladas.
   */
  private static void structuredTaskScopeExample() {
    final var weatherApi1 = new LongRunningTask("weather-api-1", 15, "some-json-1", true);
    final var weatherApi2 = new LongRunningTask("weather-api-2", 17, "some-json-2", false);
    final var weatherApi3 = new LongRunningTask("weather-api-3", 20, "some-json-3", false);

    try (var scope = new StructuredTaskScope.ShutdownOnSuccess<LongRunningTask.TaskResponse>()) {
      final StructuredTaskScope.Subtask<LongRunningTask.TaskResponse> subtask1 = scope.fork(weatherApi1);
      final StructuredTaskScope.Subtask<LongRunningTask.TaskResponse> subtask2 = scope.fork(weatherApi2);
      final StructuredTaskScope.Subtask<LongRunningTask.TaskResponse> subtask3 = scope.fork(weatherApi3);
      /**
       * Aguarda até a primeira sub-tarefa finalizar com sucesso e cancela as sub-tarefas que estão em execução.
       */
      scope.join();

      final var result = scope.result(ex -> new RuntimeException("Ocorreu um erro durante a execução das sub-tarefas", ex));
      System.out.println(result);

    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
