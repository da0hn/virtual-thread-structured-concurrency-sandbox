package dev.da0hn.structured.concurrency;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.concurrent.Callable;

public class UserRequestHandler implements Callable<String> {

  @Override
  public String call() throws Exception {

    final var result1 = this.databaseCall();
    final var result2 = this.restApiCall();

    final var result = String.format("[%s, %s]", result1, result2);
    System.out.println(result);
    return result;
  }

  private String databaseCall() throws IOException, URISyntaxException {
    System.out.println("Database: " + Thread.currentThread());
    final var networkCaller = new NetworkCaller("db-call");
    return networkCaller.makeCall(2);
  }

  private String restApiCall() throws IOException, URISyntaxException {
    System.out.println("API Rest: " + Thread.currentThread());
    final var networkCaller = new NetworkCaller("api-call");
    return networkCaller.makeCall(5);
  }



}
