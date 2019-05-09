package lpp.parser.ast;

import lpp.visitors.Visitor;

public class Union extends BinaryOp {

  public Union(Exp left, Exp right) {
    super(left, right);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return null;
  }
}
