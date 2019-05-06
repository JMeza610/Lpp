package lpp.parser;

import lpp.parser.ast.Prog;

public interface Parser {

  Prog parseProg() throws ParserException;

}