package dev.da0hn.structured.concurrency.scalability.demonstration;

public class MainJacket {

  public static void main(String[] args) {
    System.out.println("Starting main");
    for (int i = 0; i < 10_000; i++) new Thread(MainJacket::handleUserRequest).start();
    System.out.println("Ending main");
  }

  private static void handleUserRequest() {
    System.out.println("Starting thread " + Thread.currentThread());

    try {
      Thread.sleep(10_000);
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("Ending thread " + Thread.currentThread());
  }

}
