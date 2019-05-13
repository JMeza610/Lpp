package lpp.parser.ast;

import lpp.visitors.Visitor;

public class StringLiteral extends PrimLiteral<String> {

  public StringLiteral(String value) {
    super(value);
  }

  @Override
  public <T> T accept(Visitor<T> visitor) {
    return visitor.visitString(value);
  }
}
