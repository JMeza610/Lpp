package lpp.parser.ast;

import lpp.visitors.Visitor;

public class Intersect extends BinaryOp {

  public Intersect(Exp left, Exp right) {
    super(left, right);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return null;
  }
}
