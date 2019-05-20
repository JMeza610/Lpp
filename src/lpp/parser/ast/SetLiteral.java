package lpp.parser.ast;

import lpp.visitors.Visitor;

public class SetLiteral implements Exp {

  private final ExpSeq set;

  public SetLiteral(ExpSeq set) {
    this.set = set;
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "(" + set + ")";
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
      return visitor.visitSetLit(set);
  }
}
