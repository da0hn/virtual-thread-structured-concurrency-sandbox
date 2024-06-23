package dev.da0hn.structured.concurrency.threads.virtual;

public final class Utils {

  private Utils() {}

  public static void sleep(int timeoutInSeconds) {
    try {
      Thread.sleep(timeoutInSeconds);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}
