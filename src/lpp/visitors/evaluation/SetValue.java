package lpp.visitors.evaluation;

import static java.util.Objects.requireNonNull;

public class SetValue implements Value {

    private final Value setVal;

    public static final String TYPE_NAME = "SET";

    public setValue(Value setVal) {
        this.setVal = requireNonNull(setVal);
    }

    public Value getSetVal() {
        return setVal;
    }

    @Override
    public SetValue asSet() {
        return this;
    }

    @Override
    public String toString() {
        return setVal + " " + TYPE_NAME;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!(obj instanceof SetValue))
            return false;
        SetValue st = (SetValue) obj;
        return setVal.equals(st.setVal);
    }

    @Override
    public int hashCode() {
        return setVal.hashCode() * 17;
    }
}
