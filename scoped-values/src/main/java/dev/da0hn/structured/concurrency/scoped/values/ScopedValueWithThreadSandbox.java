package dev.da0hn.structured.concurrency.scoped.values;

import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;

@SuppressWarnings("preview")
public class ScopedValueWithThreadSandbox {

  public static final ScopedValue<User> USER_SCOPED_VALUE = ScopedValue.newInstance();

  public static void main(String[] args) throws Exception {

    // ScopedValue.runWhere(USER_SCOPED_VALUE, new User("Sally"), ScopedValueWithThreadSandbox::invokeThread);
    ScopedValue.where(USER_SCOPED_VALUE, new User("Sally")).call(ScopedValueWithThreadSandbox::invokeTaskScope);

  }

  private static void invokeThread() {
    try {
      ConsoleUtils.print(STR."InvokeThread - User is Bound -> \{USER_SCOPED_VALUE.isBound()}");
      var requestUser = USER_SCOPED_VALUE.get();
      String vThreadName = STR."VThread-\{requestUser.getId()}";
      final var thread = Thread.ofVirtual().name(vThreadName).start(() -> {
        ConsoleUtils.print(STR."InvokeThread - User is Bound -> \{USER_SCOPED_VALUE.isBound()}"); // Prints User is Bound -> "false"
        var user = USER_SCOPED_VALUE.orElse(new User("Anonymous"));
        ConsoleUtils.print(STR."InvokeThread - User -> \{user}"); // Prints User -> "Anonymous"
      });
      thread.join();
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }

  }

  private static String invokeTaskScope() {
    ThreadFactory threadFactory = Thread.ofVirtual().name("vThread-test-", 0).factory();
    try (var scope = new StructuredTaskScope<String>("test-scope", threadFactory)) {
      scope.fork(() -> {
        ConsoleUtils.print(STR."InvokeTaskScope - User is Bound -> \{USER_SCOPED_VALUE.isBound()}"); // Prints User is Bound -> "true"
        User requestUser = USER_SCOPED_VALUE.orElse(new User("Anonymous"));
        // Print User -> "Sally"
        // A "vThread-test-0" é filha da thread "main" e está no mesmo escopo definido na thread "main"
        ConsoleUtils.print(STR."InvokeTaskScope - User -> \{requestUser}");
        requestUser.setId("Bob"); // Altera o atributo id dentro de User (Sem alterar a referência)
        return "done";
      });
      scope.join();
    }
    catch (InterruptedException e) {
      throw new RuntimeException(e);
    }
    ConsoleUtils.print(STR."InvokeTaskScope - User is Bound -> \{USER_SCOPED_VALUE.isBound()}"); // Prints User is Bound -> "true"
    User requestUser = USER_SCOPED_VALUE.orElse(new User("Anonymous"));
    ConsoleUtils.print(STR."InvokeTaskScope - User -> \{requestUser}"); // Prints User -> "Bob"

    return "done";
  }

}
