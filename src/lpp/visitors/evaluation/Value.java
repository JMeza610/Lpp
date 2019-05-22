package lpp.visitors.evaluation;

import java.util.Set;

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

  default Set<Value> asSet() {
    throw new EvaluatorException("Expecting a set");
  }

  default String asString() {
    throw new EvaluatorException("Expecting a string");
  }

}
