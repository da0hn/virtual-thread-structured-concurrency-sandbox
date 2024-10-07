package dev.da0hn.structured.concurrency.scoped.values;

@SuppressWarnings("preview")
public class ScopeValueSandbox {

  public static final ScopedValue<User> USER_SCOPED_VALUE = ScopedValue.newInstance();

  public static void main(String[] args) throws Exception {
    ConsoleUtils.print(STR."User is Bound -> \{USER_SCOPED_VALUE.isBound()}");
    var bob = new User("Bob");
    boolean result = ScopedValue.callWhere(USER_SCOPED_VALUE, bob, ScopeValueSandbox::handleUser);
    ConsoleUtils.print(STR."Result -> \{result}");
    ConsoleUtils.print(STR."User is Bound -> \{USER_SCOPED_VALUE.isBound()}");
  }

  private static boolean handleUser() {
    ScopedUserHandler handler = new ScopedUserHandler();
    return handler.handle();
  }

}
