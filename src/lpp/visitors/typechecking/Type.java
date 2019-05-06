package lpp.visitors.typechecking;

public interface Type {
  default Type checkEqual(Type found) throws TypecheckerException {
    if (!equals(found))
      throw new TypecheckerException(found.toString(), toString());
    return this;
  }

  default void checkIsPairType() throws TypecheckerException {
    if (!(this instanceof PairType))
      throw new TypecheckerException(toString(), PairType.TYPE_NAME);
  }

  default Type getFstPairType() throws TypecheckerException {
    checkIsPairType();
    return ((PairType) this).getFstType();
  }

  default Type getSndPairType() throws TypecheckerException {
    checkIsPairType();
    return ((PairType) this).getSndType();
  }
}
