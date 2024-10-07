package dev.da0hn.structured.concurrency.scoped.values;

@SuppressWarnings("preview")
public class ScopedUserHandler {

  public boolean handle() {
    boolean isBound = ScopeValueSandbox.USER_SCOPED_VALUE.isBound();
    ConsoleUtils.print(STR."Handler - User is Bound -> \{isBound}");
    if (isBound) {
      final var user = ScopeValueSandbox.USER_SCOPED_VALUE.get();
      ConsoleUtils.print(STR."Handler - User -> \{user}");
    }
    return isBound;
  }

}
