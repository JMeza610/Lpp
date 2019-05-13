package lpp.visitors;

import lpp.parser.ast.Block;
import lpp.parser.ast.Exp;
import lpp.parser.ast.Ident;
import lpp.parser.ast.Stmt;
import lpp.parser.ast.StmtSeq;

public interface Visitor<T> {
  T visitAdd(Exp left, Exp right);

  T visitAssignStmt(Ident ident, Exp exp);

  T visitIntLiteral(int value);

  T visitEq(Exp left, Exp right);

  T visitMoreStmt(Stmt first, StmtSeq rest);

  T visitMul(Exp left, Exp right);

  T visitPrintStmt(Exp exp);

  T visitProg(StmtSeq stmtSeq);

  T visitSign(Exp exp);

  T visitIdent(Ident id); // the only corner case ...

  T visitSingleStmt(Stmt stmt);

  T visitDecStmt(Ident ident, Exp exp);

  T visitNot(Exp exp);

  T visitAnd(Exp left, Exp right);

  T visitBoolLiteral(boolean value);

  T visitIfStmt(Exp exp, Block thenBlock, Block elseBlock);

  T visitBlock(StmtSeq stmtSeq);

  T visitPairLit(Exp left, Exp right);

  T visitFst(Exp exp);

  T visitSnd(Exp exp);

  //new visitors
  T visitWhileStmt(Exp exp, Block block);

  T visitIn(Exp left, Exp right);

  T visitUnion(Exp left, Exp right);

  T visitIntersect(Exp left, Exp right);

  T visitCat(Exp left, Exp right);

  T visitString(String value);

  T visitDim(Exp exp);
}
