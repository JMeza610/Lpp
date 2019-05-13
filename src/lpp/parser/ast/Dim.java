package lpp.parser.ast;

import lpp.visitors.Visitor;

public class Dim extends UnaryOp {

  public Dim(Exp exp) {
    super(exp);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitDim(exp);
  }
}
