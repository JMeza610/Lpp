package lpp.parser.ast;

import lpp.visitors.Visitor;

public class WhileStmt implements Stmt {

  private final Exp exp;
  private final Block block;

  public WhileStmt(Exp exp, Block block) {
    this.exp = exp;
    this.block = block;
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitWhileStmt(exp, block);
  }
}
