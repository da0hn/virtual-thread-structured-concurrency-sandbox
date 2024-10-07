package dev.da0hn.structured.concurrency.scoped.values;

@SuppressWarnings("preview")
public class ScopedValueRebindSandbox {

  public static final ScopedValue<User> USER_SCOPED_VALUE = ScopedValue.newInstance();

  public static void main(String[] args) {
    ConsoleUtils.print(STR."User is Bound -> \{USER_SCOPED_VALUE.isBound()}");
    var bob = new User("Bob");
    ScopedValue.runWhere(USER_SCOPED_VALUE, bob, ScopedValueRebindSandbox::handleUser);
    ConsoleUtils.print(STR."User is Bound -> \{USER_SCOPED_VALUE.isBound()}");
  }

  private static void handleUser() {
    ConsoleUtils.print(STR."HandleUser - User -> \{USER_SCOPED_VALUE.get()}");
    ScopedValue.runWhere(USER_SCOPED_VALUE, new User("Anonymous"), ScopedValueRebindSandbox::callAsAnonymous);
    ConsoleUtils.print(STR."HandleUser - User -> \{USER_SCOPED_VALUE.get()}");
  }

  private static void callAsAnonymous() {
    ConsoleUtils.print(STR."HandleUser - User -> \{USER_SCOPED_VALUE.get()}");
  }

}
