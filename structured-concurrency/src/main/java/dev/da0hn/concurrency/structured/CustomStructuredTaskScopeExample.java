package dev.da0hn.concurrency.structured;

import dev.da0hn.concurrency.structured.LongRunningTask;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.StructuredTaskScope.Subtask.State;
import java.util.concurrent.TimeUnit;

@SuppressWarnings("preview")
public class CustomStructuredTaskScopeExample {

  public static void main(String[] args) {
    System.out.println("> Main thread started: " + Thread.currentThread().getName());

    structuredTaskScopeExample();

    System.out.println("> Main thread ended: " + Thread.currentThread().getName());
  }



  private static void structuredTaskScopeExample() {
    final var weatherApi1 = new LongRunningTask("Weather-api-1", 3, "30", true);
    final var weatherApi2 = new LongRunningTask("Weather-api-2", 4, "32", true);
    final var weatherApi3 = new LongRunningTask("Weather-api-3", 5, "34", false);
    final var weatherApi4 = new LongRunningTask("Weather-api-4", 6, "34", false);
    final var weatherApi5 = new LongRunningTask("Weather-api-5", 9, "30", false);

    try (var scope = new AverageWeatherTaskScope()) {
      final var subtask1 = scope.fork(weatherApi1);
      final var subtask2 = scope.fork(weatherApi2);
      final var subtask3 = scope.fork(weatherApi3);
      final var subtask4 = scope.fork(weatherApi4);
      final var subtask5 = scope.fork(weatherApi5);
      scope.join();

      final var response = scope.response();
      System.out.println(response);

    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

}
