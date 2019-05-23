package lpp.visitors.evaluation;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class SetValue implements Value {

  private HashSet<Value> set = new HashSet<>();

  public SetValue(Value value) {
    set.add(value);
  }

  public SetValue(HashSet<Value> set) {
    this.set.addAll(set);
  }

  public SetValue(Value value, Set<Value> moreValues) {
    this(value);
    set.addAll(moreValues);
  }

  @Override
  public HashSet<Value> asSet() {
    return set;
  }

  @Override
  public String toString() {
    Iterator<Value> it = set.iterator();
    if (!it.hasNext())
      return "{ }";

    StringBuilder sb = new StringBuilder();
    sb.append('{');
    while (it.hasNext()) {
      sb.append(it.next());
      if (!it.hasNext()) break;
      sb.append(',').append(' ');
    }
    return sb.append('}').toString();
  }


  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (!(obj instanceof SetValue))
      return false;
    SetValue st = (SetValue) obj;
    return set.equals(st.set);
  }

  @Override
  public int hashCode() {
    return set.hashCode() * 17;
  }
}
