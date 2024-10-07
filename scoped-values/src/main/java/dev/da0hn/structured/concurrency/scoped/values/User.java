package dev.da0hn.structured.concurrency.scoped.values;

import java.util.Objects;

@SuppressWarnings("preview")
public final class User {

  private String id;

  public User(String id) { this.id = id; }

  public String getId() { return id; }

  public String setId(String id) { return this.id = id; }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == this) return true;
    if (obj == null || obj.getClass() != this.getClass()) return false;
    var that = (User) obj;
    return Objects.equals(this.id, that.id);
  }

  @Override
  public String toString() {
    return STR."User[id=\{id}]";
  }

}
