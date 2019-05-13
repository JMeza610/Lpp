package lpp.parser.ast;

import lpp.visitors.Visitor;

public class Cat extends BinaryOp {

  public Cat(Exp left, Exp right) {
    super(left, right);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
      return visitor.visitCat(left, right);
  }
}
