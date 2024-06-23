package dev.da0hn.structured.concurrency.threads.virtual;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MainJacket {

  public static void main(String[] args) throws InterruptedException {
    System.out.println("Starting main " + Thread.currentThread());

    List<Thread> activeVirtualThreads = new ArrayList<>();

    final var vThreadBuilder = Thread.ofVirtual().name("temp-task-", 0);

    for (int i = 0; i < 100_000; i++) {
      activeVirtualThreads.add(startThread(vThreadBuilder));
    }
    // Join on main thread
    for (Thread activeVirtualThread : activeVirtualThreads) {
      activeVirtualThread.join();
    }

    System.out.println("Ending main " + Thread.currentThread());
  }

  private static Thread startThread(Thread.Builder.OfVirtual vThreadBuilder) {
    // return Thread.ofVirtual().start(MainJacket::handleUserRequest);
    // return Thread.startVirtualThread(MainJacket::handleUserRequest);
    return vThreadBuilder.start(MainJacket::handleUserRequest);
  }

  private static void handleUserRequest() {
    System.out.println("Starting thread " + Thread.currentThread());

    Utils.sleep(ThreadLocalRandom.current().nextInt(10, 45));
    System.out.println("Ending thread " + Thread.currentThread());
  }

}
