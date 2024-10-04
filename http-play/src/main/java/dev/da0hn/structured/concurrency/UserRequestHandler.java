package dev.da0hn.structured.concurrency;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class UserRequestHandler implements Callable<String> {

  @Override
  public String call() throws Exception {
    return callWithCompletableFuture();
  }

  private String callWithCompletableFuture() {
    try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
      final var start = Instant.now();

      final var result = CompletableFuture.supplyAsync(this::databaseCall, executorService)
        .thenCombineAsync(
          CompletableFuture.supplyAsync(this::restApiCall, executorService),
          (db, api) -> String.format("[%s, %s]", db, api)
        ).join();

      final var end = Instant.now();
      System.out.println(result + " " + ChronoUnit.MILLIS.between(start, end) + "ms");
      return result;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String callWithFuture() {
    try (var executorService = Executors.newVirtualThreadPerTaskExecutor()) {
      final var start = Instant.now();
      final var result = executorService.invokeAll(Set.of(this::databaseCall, this::restApiCall)).stream()
        .map(future -> {
          try { return (String) future.get(); }
          catch (Exception e) { throw new RuntimeException(e); }
        }).collect(Collectors.joining(", ", "[", "]"));

      final var end = Instant.now();
      System.out.println(result + " " + ChronoUnit.MILLIS.between(start, end) + "ms");

      return result;
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private String sequentialCall() {
    final var start = Instant.now();
    final var result1 = this.databaseCall();
    final var result2 = this.restApiCall();
    final var end = Instant.now();
    final var result = String.format(
      "[%s, %s], duration: %dms",
      result1,
      result2,
      ChronoUnit.MILLIS.between(start, end)
    );
    System.out.println(result);
    return result;
  }

  private String databaseCall() {
    try {
      System.out.println("Database: " + Thread.currentThread());
      final var networkCaller = new NetworkCaller("db-call");
      return networkCaller.makeCall(2);
    }
    catch (IOException | URISyntaxException e) {
      return null;
    }
  }

  private String restApiCall() {
    try {
      System.out.println("API Rest: " + Thread.currentThread());
      final var networkCaller = new NetworkCaller("api-call");
      return networkCaller.makeCall(5);
    }
    catch (IOException | URISyntaxException e) {
      return null;
    }
  }

}
