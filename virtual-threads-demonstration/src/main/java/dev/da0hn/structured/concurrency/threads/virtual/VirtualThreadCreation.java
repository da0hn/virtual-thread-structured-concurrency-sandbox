package dev.da0hn.structured.concurrency.threads.virtual;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;

public final class VirtualThreadCreation {

  public static void main(String[] args) throws InterruptedException {
    createVirtualThreadUsingFactories();
  }

  private static void createVirtualThreadUsingFactories() throws InterruptedException {
    // Thread-safe virtual thread creation
    final ThreadFactory vThreadFactory = Thread.ofVirtual().name("vThread-", 0).factory();

    final Thread vThread1 = vThreadFactory.newThread(VirtualThreadCreation::handleUserRequest);
    vThread1.start();
    final Thread vThread2 = vThreadFactory.newThread(VirtualThreadCreation::handleUserRequest);
    vThread2.start();

    vThread1.join();
    vThread2.join();

  }

  private static void createVirtualThreadUsingBuilder() throws InterruptedException {
    // Non Thread-safe virtual thread creation
    final var vThreadBuilder = Thread.ofVirtual().name("vThread-", 0);

    final Thread vThread1 = vThreadBuilder.start(VirtualThreadCreation::handleUserRequest);
    final Thread vThread2 = vThreadBuilder.start(VirtualThreadCreation::handleUserRequest);

    vThread1.join();
    vThread2.join();
  }

  private static void createVirtualThreadUsingExecutorService() {

    //    try (ExecutorService srv = Executors.newVirtualThreadPerTaskExecutor()) -> vThread with by default

    final ThreadFactory vThreadFactory = Thread.ofVirtual().name("vThread-", 0).factory();
    // Create an Virtual Thread using ExecutorService
    // Note the try-with-resources which will make sure all the Virtual Threads are completed before the main thread exits
    try (ExecutorService srv = Executors.newThreadPerTaskExecutor(vThreadFactory)) { // receive vThread factory
      final Future<?> submit1 = srv.submit(VirtualThreadCreation::handleUserRequest);
      final Future<?> submit2 = srv.submit(VirtualThreadCreation::handleUserRequest);
    }
  }

  private static void handleUserRequest() {
    System.out.println("Starting thread " + Thread.currentThread());

    Utils.sleep(ThreadLocalRandom.current().nextInt(10, 45));

    System.out.println("Ending thread " + Thread.currentThread());
  }

}
