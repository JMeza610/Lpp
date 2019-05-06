package lpp.visitors.evaluation;

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
}
