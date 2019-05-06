package lpp.parser.ast;

import lpp.visitors.Visitor;

public class DecStmt extends AbstractAssignStmt {

  public DecStmt(Ident ident, Exp exp) {
    super(ident, exp);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitDecStmt(ident, exp);
  }

}
