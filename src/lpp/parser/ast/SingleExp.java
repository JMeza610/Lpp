package lpp.parser.ast;

import lpp.visitors.Visitor;

public class SingleExp extends Single<Exp> implements ExpSeq {

  public SingleExp(Exp exp) {
    super(exp);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return null;
  }
}
