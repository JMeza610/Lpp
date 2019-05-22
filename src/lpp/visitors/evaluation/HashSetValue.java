package lpp.visitors.evaluation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class HashSetValue implements Value, Iterable<Value> {

  private HashSet<Value> set = new HashSet<>();

  public HashSetValue(Value value) {
    set.add(value);
  }

  public HashSetValue(Value value, Set moreValues) {
    this(value);
    set.addAll(moreValues);
  }

  @Override
  public HashSet<Value> asSet() {
    return set;
  }

  @Override
  public Iterator<Value> iterator() {
    return this.set.iterator();
  }

  @Override
  public String toString() {
    return set.toString();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof HashSetValue))
      return false;
    HashSetValue st = (HashSetValue) obj;
    return set.equals(st.set);
  }

  @Override
  public int hashCode() {
    return set.hashCode() * 17;
  }
}
