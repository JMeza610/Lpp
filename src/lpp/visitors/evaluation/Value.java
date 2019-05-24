package lpp.visitors.evaluation;

import java.util.HashSet;

public interface Value {
  /* default conversion methods */
  default int asInt() {
    throw new EvaluatorException("Expecting an integer");
  }

  default boolean asBool() {
    throw new EvaluatorException("Expecting a boolean");
  }

  default PairValue asPair() {
    throw new EvaluatorException("Expecting a pair");
  }

  default HashSet<Value> asSet() {
    throw new EvaluatorException("Expecting a set");
  }

  default String asString() {
    throw new EvaluatorException("Expecting a string");
  }

}
