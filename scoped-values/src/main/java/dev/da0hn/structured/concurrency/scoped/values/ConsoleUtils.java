package dev.da0hn.structured.concurrency.scoped.values;

@SuppressWarnings("preview")
public final class ConsoleUtils {

  public static void print(String message) {
    System.out.println(STR."[\{Thread.currentThread().getName()}] \{message}");
  }

}
