package lpp.visitors.typechecking;

public interface Type {
  default Type checkEqual(Type found) throws TypecheckerException {
    if (!equals(found))
      throw new TypecheckerException(found.toString(), toString());
    return this;
  }

  /**
   * Checks whether this exp is either Set or String
   *
   * @throws TypecheckerException
   */
  default void checkStringOrSet() throws TypecheckerException {
    if (!equals(PrimtType.STRING) && !(this instanceof SetType))
      throw new TypecheckerException(toString(), "STRING or SET");
  }

  /**
   * @return This type
   * @throws TypecheckerException
   */
  default Type checkIsSetType() throws TypecheckerException {
    if (!(this instanceof SetType))
      throw new TypecheckerException(toString(), SetType.TYPE_NAME);
    return this;
  }

  /**
   * @return Element type of this set
   * @throws TypecheckerException
   */
  default Type getSetType() throws TypecheckerException {
    checkIsSetType();
    return ((SetType) this).getSet();
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
