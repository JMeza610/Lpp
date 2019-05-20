package lpp.visitors.typechecking;

public class SetType implements Type {

    private final Type set;

    public static final String TYPE_NAME = "SET";

    public SetType(Type set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return set + " " + TYPE_NAME;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SetType)) return false;
        SetType st = (SetType) obj;
        return set.equals(st.set);
    }

    @Override
    public int hashCode() {
        return set.hashCode() * 17;
    }

    public Type getSet() {
        return set;
    }
}
