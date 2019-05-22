package lpp.visitors.evaluation;

import lpp.environments.EnvironmentException;
import lpp.environments.GenEnvironment;
import lpp.parser.*;
import lpp.parser.ast.*;
import lpp.visitors.Visitor;
import lpp.visitors.typechecking.TypeCheck;
import lpp.visitors.typechecking.TypecheckerException;

import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashSet;

import static java.lang.System.err;
import static java.util.Objects.requireNonNull;

public class Eval implements Visitor<Value> {

  private final GenEnvironment<Value> env = new GenEnvironment<>();
  private final PrintWriter printWriter;

  public Eval() {
    printWriter = new PrintWriter(System.out, true);
  }

  public Eval(PrintWriter printWriter) {
    this.printWriter = requireNonNull(printWriter);
  }

  // dynamic semantics for programs; no value returned by the visitor

  @Override
  public Value visitProg(StmtSeq stmtSeq) {
    try {
      stmtSeq.accept(this);
      // possible runtime errors
      // EnvironmentException: undefined variable
    } catch (EnvironmentException e) {
      throw new EvaluatorException(e);
    }
    return null;
  }

  // dynamic semantics for statements; no value returned by the visitor

  @Override
  public Value visitAssignStmt(Ident ident, Exp exp) {
    env.update(ident, exp.accept(this));
    return null;
  }

  @Override
  public Value visitPrintStmt(Exp exp) {
    printWriter.println(exp.accept(this));
    return null;
  }

  @Override
  public Value visitDecStmt(Ident ident, Exp exp) {
    env.dec(ident, exp.accept(this));
    return null;
  }

  @Override
  public Value visitIfStmt(Exp exp, Block thenBlock, Block elseBlock) {
    if (exp.accept(this).asBool()) {
      thenBlock.accept(this);
    } else if (elseBlock != null)
      elseBlock.accept(this);
    return null;
  }

  @Override
  public Value visitBlock(StmtSeq stmtSeq) {
    env.enterScope();
    stmtSeq.accept(this);
    env.exitScope();
    return null;
  }

  // dynamic semantics for sequences of statements
  // no value returned by the visitor

  @Override
  public Value visitSingleStmt(Stmt stmt) {
    stmt.accept(this);
    return null;
  }

  @Override
  public Value visitMoreStmt(Stmt first, StmtSeq rest) {
    first.accept(this);
    rest.accept(this);
    return null;
  }

  // dynamic semantics of expressions; a value is returned by the visitor

  @Override
  public Value visitAdd(Exp left, Exp right) {
    return new IntValue(left.accept(this).asInt() + right.accept(this).asInt());
  }

  @Override
  public Value visitIntLiteral(int value) {
    return new IntValue(value);
  }

  @Override
  public Value visitMul(Exp left, Exp right) {
    return new IntValue(left.accept(this).asInt() * right.accept(this).asInt());
  }

  @Override
  public Value visitSign(Exp exp) {
    return new IntValue(-exp.accept(this).asInt());
  }

  @Override
  public Value visitIdent(Ident id) {
    return env.lookup(id);
  }

  @Override
  public Value visitNot(Exp exp) {
    return new BoolValue(!exp.accept(this).asBool());
  }

  @Override
  public Value visitAnd(Exp left, Exp right) {
    return new BoolValue(left.accept(this).asBool() && right.accept(this).asBool());
  }

  @Override
  public Value visitBoolLiteral(boolean value) {
    return new BoolValue(value);
  }

  @Override
  public Value visitEq(Exp left, Exp right) {
    return new BoolValue(left.accept(this).equals(right.accept(this)));
  }

  @Override
  public Value visitPairLit(Exp left, Exp right) {
    return new PairValue(left.accept(this), right.accept(this));
  }

  @Override
  public Value visitFst(Exp exp) {
    return exp.accept(this).asPair().getFstVal();
  }

  @Override
  public Value visitSnd(Exp exp) {
    return exp.accept(this).asPair().getSndVal();
  }

  @Override
  public Value visitWhileStmt(Exp exp, Block block) {
    while (exp.accept(this).asBool())
      block.accept(this);
    return null;
  }

  @Override
  public Value visitString(String value) {
    return new StringValue(value);
  }

  @Override
  public Value visitCat(Exp left, Exp right) {
    return new StringValue(left.accept(this).asString().concat(right.accept(this).asString()));
  }

  @Override
  public Value visitIn(Exp left, Exp right) {
    return new BoolValue(right.accept(this).asSet().contains(left.accept(this)));
  }

  @Override
  public Value visitUnion(Exp left, Exp right) {
    HashSet<Value> hashSet = new HashSet<>(left.accept(this).asSet());
    hashSet.addAll(right.accept(this).asSet());
    return new SetValue(hashSet);
  }

  @Override
  public Value visitIntersect(Exp left, Exp right) {
    HashSet<Value> hashSet = new HashSet<>(left.accept(this).asSet());
    hashSet.retainAll(right.accept(this).asSet());
    return new SetValue(hashSet);
  }

  @Override
  public Value visitDim(Exp exp) {
    Value val = exp.accept(this);
    if (val instanceof SetValue)
      return new IntValue(val.asSet().size());
    return new IntValue(val.asString().length());
  }

  @Override
  public Value visitSetLit(ExpSeq set) {
    return set.accept(this);
  }

  @Override
  public Value visitSingleExp(Exp exp) {
    return new SetValue(exp.accept(this));
  }

  @Override
  public Value visitMoreExp(Exp first, ExpSeq rest) {
    return new SetValue(first.accept(this), rest.accept(this).asSet());
  }

  public static void main(String[] args) {
    try (Tokenizer tokenizer = new StreamTokenizer(new InputStreamReader(System.in))) {
      Parser parser = new MyParser(tokenizer);
      Prog prog = parser.parseProg();
      prog.accept(new TypeCheck());
      prog.accept(new Eval());
    } catch (TokenizerException e) {
      err.println("Tokenizer error: " + e.getMessage());
    } catch (ParserException e) {
      err.println("Syntax error: " + e.getMessage());
    } catch (TypecheckerException e) {
      err.println("Static error: " + e.getMessage());
    } catch (EvaluatorException e) {
      err.println("Dynamic error: " + e.getMessage());
    } catch (Throwable e) {
      err.println("Unexpected error.");
      e.printStackTrace();
    }
  }

}
