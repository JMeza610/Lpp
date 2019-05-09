package lpp.parser;

import static java.util.Objects.requireNonNull;
import static lpp.parser.TokenType.*;

import lpp.parser.ast.*;

/*
Prog ::= StmtSeq 'EOF'
 StmtSeq ::= Stmt (';' StmtSeq)?
 Stmt ::= 'let'? ID '=' Exp | 'print' Exp |  'if' '(' Exp ')' '{' StmtSeq '}' ('else' '{' StmtSeq '}')? | 'while' '(' Exp ')' '{' StmtSeq '}'
 ExpSeq ::= Exp (',' ExpSeq)?
 Exp ::= Eq ('&&' Eq)*
 Eq ::= In ('==' In)*
 In ::= Union ('in' Union)*
 Union ::= Intersect ('\/' Intersect)*
 Intersect ::= Cat ('/\' Cat)*
 Cat ::= Add ('^' Add)*
 Add ::= Mul ('+' Mul)*
 Mul::= Atom ('*' Atom)*
 Atom ::= '[' Exp ',' Exp ']' | 'fst' Atom | 'snd' Atom | '-' Atom | '!' Atom | BOOL | NUM | ID | '(' Exp ')' | '{' ExpSeq '}' | '#' Atom
*/

public class MyParser implements Parser {

  private final Tokenizer tokenizer;

  private void tryNext() throws ParserException {
    try {
      tokenizer.next();
    } catch (TokenizerException e) {
      throw new ParserException(e);
    }
  }

  private void match(TokenType expected) throws ParserException {
    final TokenType found = tokenizer.tokenType();
    if (found != expected)
      throw new ParserException(
          "Expecting " + expected + ", found " + found + "('" + tokenizer.tokenString() + "')");
  }

  private void consume(TokenType expected) throws ParserException {
    match(expected);
    tryNext();
  }

  private void unexpectedTokenError() throws ParserException {
    throw new ParserException("Unexpected token " + tokenizer.tokenType() + "('" + tokenizer.tokenString() + "')");
  }

  public MyParser(Tokenizer tokenizer) {
    this.tokenizer = requireNonNull(tokenizer);
  }

  @Override
  public Prog parseProg() throws ParserException {
    tryNext(); // one look-ahead symbol
    Prog prog = new ProgClass(parseStmtSeq());
    match(EOF);
    return prog;
  }

  private StmtSeq parseStmtSeq() throws ParserException {
    Stmt stmt = parseStmt();
    if (tokenizer.tokenType() == STMT_SEP) {
      tryNext();
      return new MoreStmt(stmt, parseStmtSeq());
    }
    return new SingleStmt(stmt);
  }

  private Stmt parseStmt() throws ParserException {
    switch (tokenizer.tokenType()) {
      default:
        unexpectedTokenError();
      case PRINT:
        return parsePrintStmt();
      case LET:
        return parseVarStmt();
      case IDENT:
        return parseAssignStmt();
      case IF:
        return parseIfStmt();
      case WHILE:
        return parseWhileStmt();
    }
  }

  private PrintStmt parsePrintStmt() throws ParserException {
    consume(PRINT); // or tryNext();
    return new PrintStmt(parseExp());
  }

  private DecStmt parseVarStmt() throws ParserException {
    consume(LET); // or tryNext();
    Ident ident = parseIdent();
    consume(ASSIGN);
    return new DecStmt(ident, parseExp());
  }

  private AssignStmt parseAssignStmt() throws ParserException {
    Ident ident = parseIdent();
    consume(ASSIGN);
    return new AssignStmt(ident, parseExp());
  }

  private WhileStmt parseWhileStmt() throws ParserException {
    consume(WHILE);
    consume(OPEN_PAR);
    Exp exp = parseExp();
    consume(CLOSE_PAR);
    return new WhileStmt(exp, parseBlock());
  }

  private IfStmt parseIfStmt() throws ParserException {
    consume(IF); // or tryNext();
    consume(OPEN_PAR);
    Exp exp = parseExp();
    consume(CLOSE_PAR);
    Block thenBlock = parseBlock();
    if (tokenizer.tokenType() != ELSE)
      return new IfStmt(exp, thenBlock);
    consume(ELSE); // or tryNext();
    Block elseBlock = parseBlock();
    return new IfStmt(exp, thenBlock, elseBlock);
  }

  private Block parseBlock() throws ParserException {
    consume(OPEN_BLOCK);
    StmtSeq stmts = parseStmtSeq();
    consume(CLOSE_BLOCK);
    return new Block(stmts);
  }

  private Exp parseExp() throws ParserException {
    Exp exp = parseEq();
    while (tokenizer.tokenType() == AND) {
      tryNext();
      exp = new And(exp, parseEq());
    }
    return exp;
  }

  private Exp parseEq() throws ParserException {
    Exp exp = parseIn();
    while (tokenizer.tokenType() == EQ) {
      tryNext();
      exp = new Eq(exp, parseIn());
    }
    return exp;
  }

  private Exp parseIn() throws ParserException {
    Exp exp = parseUnion();
    while (tokenizer.tokenType() == IN) {
      tryNext();
      exp = new In(exp, parseUnion());
    }
    return exp;
  }

  private Exp parseUnion() throws ParserException {
    Exp exp = parseIntersect();
    while (tokenizer.tokenType() == UNION) {
      tryNext();
      exp = new Union(exp, parseIntersect());
    }
    return exp;
  }

  private Exp parseIntersect() throws ParserException {
    Exp exp = parseCat();
    while (tokenizer.tokenType() == INTERSECT) {
      tryNext();
      exp = new Intersect(exp, parseCat());
    }
    return exp;
  }

  private Exp parseCat() throws ParserException {
    Exp exp = parseAdd();
    while (tokenizer.tokenType() == CAT) {
      tryNext();
      exp = new Cat(exp, parseAdd());
    }
    return exp;
  }

  private Exp parseAdd() throws ParserException {
    Exp exp = parseMul();
    while (tokenizer.tokenType() == PLUS) {
      tryNext();
      exp = new Add(exp, parseMul());
    }
    return exp;
  }

  private Exp parseMul() throws ParserException {
    Exp exp = parseAtom();
    while (tokenizer.tokenType() == TIMES) {
      tryNext();
      exp = new Mul(exp, parseAtom());
    }
    return exp;
  }

  private Exp parseAtom() throws ParserException {
    switch (tokenizer.tokenType()) {
      default:
        unexpectedTokenError();
      case NUM:
        return parseNum();
      case IDENT:
        return parseIdent();
      case STRING:
        return parseString();
      case MINUS:
        return parseMinus();
      case OPEN_PAR:
        return parseRoundPar();
      case BOOL:
        return parseBoolean();
      case NOT:
        return parseNot();
      case OPEN_PAIR:
        return parsePairLit();
      case FST:
        return parseFst();
      case SND:
        return parseSnd();
      case DIM:
        return parseDim();
      //case OPEN_BLOCK:
      //return parseSetLit();
    }
  }

  private IntLiteral parseNum() throws ParserException {
    int val = tokenizer.intValue();
    consume(NUM); // or tryNext();
    return new IntLiteral(val);
  }

  private BoolLiteral parseBoolean() throws ParserException {
    boolean val = tokenizer.boolValue();
    consume(BOOL); // or tryNext();
    return new BoolLiteral(val);
  }

  private StringLiteral parseString() throws ParserException {
    String val = tokenizer.stringValue();
    consume(STRING);
    return new StringLiteral(val);
  }
  private Ident parseIdent() throws ParserException {
    String name = tokenizer.tokenString();
    consume(IDENT); // or tryNext();
    return new SimpleIdent(name);
  }

  private Sign parseMinus() throws ParserException {
    consume(MINUS); // or tryNext();
    return new Sign(parseAtom());
  }

  private Fst parseFst() throws ParserException {
    consume(FST); // or tryNext();
    return new Fst(parseAtom());
  }

  private Snd parseSnd() throws ParserException {
    consume(SND); // or tryNext();
    return new Snd(parseAtom());
  }

  private Not parseNot() throws ParserException {
    consume(NOT); // or tryNext();
    return new Not(parseAtom());
  }

  private Dim parseDim() throws ParserException {
    consume(DIM);
    return new Dim(parseAtom());
  }

  /*
    private SetLit() throws ParserException {
      return null;
    }
  */
  private PairLit parsePairLit() throws ParserException {
    consume(OPEN_PAIR); // or tryNext();
    Exp left = parseExp();
    consume(EXP_SEP);
    Exp right = parseExp();
    consume(CLOSE_PAIR);
    return new PairLit(left, right);
  }

  private Exp parseRoundPar() throws ParserException {
    consume(OPEN_PAR); // or tryNext();
    Exp exp = parseExp();
    consume(CLOSE_PAR);
    return exp;
  }

}
