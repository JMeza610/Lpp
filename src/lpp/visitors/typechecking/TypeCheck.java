package lpp.visitors.typechecking;

import lpp.environments.EnvironmentException;
import lpp.environments.GenEnvironment;
import lpp.parser.ast.*;
import lpp.visitors.Visitor;

import static lpp.visitors.typechecking.PrimtType.*;

public class TypeCheck implements Visitor<Type> {

  private final GenEnvironment<Type> env = new GenEnvironment<>();

  private void checkBinOp(Exp left, Exp right, Type type) {
    type.checkEqual(left.accept(this));
    type.checkEqual(right.accept(this));
  }

  // static semantics for programs; no value returned by the visitor

  @Override
  public Type visitProg(StmtSeq stmtSeq) {
    try {
      stmtSeq.accept(this);
    } catch (EnvironmentException e) { // undefined variable
      throw new TypecheckerException(e);
    }
    return null;
  }

  // static semantics for statements; no value returned by the visitor

  @Override
  public Type visitAssignStmt(Ident ident, Exp exp) {
    Type found = env.lookup(ident);
    found.checkEqual(exp.accept(this));
    return null;
  }

  @Override
  public Type visitPrintStmt(Exp exp) {
    exp.accept(this);
    return null;
  }

  @Override
  public Type visitDecStmt(Ident ident, Exp exp) {
    env.dec(ident, exp.accept(this));
    return null;
  }

  @Override
  public Type visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
    BOOL.checkEqual(exp.accept(this));
    thenBlock.accept(this);
    if (elseBlock == null)
      return null;
    elseBlock.accept(this);
    return null;
  }

  @Override
  public Type visitBlock(StmtSeq stmtSeq) {
    env.enterScope();
    stmtSeq.accept(this);
    env.exitScope();
    return null;
  }

  // static semantics for sequences of statements
  // no value returned by the visitor

  @Override
  public Type visitSingleStmt(Stmt stmt) {
    stmt.accept(this);
    return null;
  }

  @Override
  public Type visitMoreStmt(Stmt first, StmtSeq rest) {
    first.accept(this);
    rest.accept(this);
    return null;
  }

  // static semantics of expressions; a type is returned by the visitor

  @Override
  public Type visitAdd(Exp left, Exp right) {
    checkBinOp(left, right, INT);
    return INT;
  }

  @Override
  public Type visitIntLiteral(int value) {
    return INT;
  }

  @Override
  public Type visitMul(Exp left, Exp right) {
    checkBinOp(left, right, INT);
    return INT;
  }

  @Override
  public Type visitSign(Exp exp) {
    return INT.checkEqual(exp.accept(this));
  }

  @Override
  public Type visitIdent(Ident id) {
    return env.lookup(id);
  }

  @Override
  public Type visitNot(Exp exp) {
    return BOOL.checkEqual(exp.accept(this));
  }

  @Override
  public Type visitAnd(Exp left, Exp right) {
    checkBinOp(left, right, BOOL);
    return BOOL;
  }

  @Override
  public Type visitBoolLiteral(boolean value) {
    return BOOL;
  }

  @Override
  public Type visitEq(Exp left, Exp right) {
    left.accept(this).checkEqual(right.accept(this));
    return BOOL;
  }

  @Override
  public Type visitPairLit(Exp left, Exp right) {
    return new PairType(left.accept(this), right.accept(this));
  }

  @Override
  public Type visitFst(Exp exp) {
    return exp.accept(this).getFstPairType();
  }

  @Override
  public Type visitSnd(Exp exp) {
    return exp.accept(this).getSndPairType();
  }

  //new visitors
  @Override
  public Type visitWhileStmt(Exp exp, Block block) {
    BOOL.checkEqual(exp.accept(this));
    block.accept(this);
    return null;
  }

  @Override
  public Type visitIn(Exp left, Exp right) {
    SetType leftSet = new SetType(left.accept(this));
    Type rightType = right.accept(this);
    leftSet.checkEqual(rightType);
    return BOOL;
  }

  @Override
  public Type visitUnion(Exp left, Exp right) {
    Type leftType = left.accept(this).checkIsSetType();
    Type rightType = right.accept(this).checkIsSetType();
    leftType.checkEqual(rightType);
    return leftType; // it doesn't matter which one you return when they are the same type
  }

  @Override
  public Type visitIntersect(Exp left, Exp right) {
    Type leftType = left.accept(this).checkIsSetType();
    Type rightType = right.accept(this).checkIsSetType();
    leftType.checkEqual(rightType);
    return leftType; // it doesn't matter which one you return when they are the same type
  }

  @Override
  public Type visitCat(Exp left, Exp right) {
    checkBinOp(left, right, STRING);
    return STRING;
  }

  @Override
  public Type visitString(String value) {
    return STRING;
  }

  @Override
  public Type visitDim(Exp exp) {
    exp.accept(this).checkStringOrSet();
    return INT;
  }

  @Override
  public Type visitSetLit(ExpSeq set) {
    return new SetType(set.accept(this));
  }

  @Override
  public Type visitSingleExp(Exp exp) {
    return exp.accept(this);
  }

  @Override
  public Type visitMoreExp(Exp first, ExpSeq rest) {
    return first.accept(this).checkEqual(rest.accept(this));
  }

}
