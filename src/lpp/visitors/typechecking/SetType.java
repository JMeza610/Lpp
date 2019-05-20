package lpp.visitors.typechecking;

public class SetType implements Type {

    private final Type set;

    public static final String TYPE_NAME = "SET";

    public SetType(Type set) {
        this.set = set;
    }

    @Override
    public String toString() {
        return set + "SET";
    }
}
