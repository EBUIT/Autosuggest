package framework.bean.search;

/**
 * Created by metairie on 15-Apr-15.
 */
public class Eq extends Term {

    private boolean operator;

    Eq(String identifier, Object value, TermType eq, boolean operator) {
        super(identifier, value, eq);
        this.operator = operator;
    }

    public boolean isOperator() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Eq)) return false;
        if (!super.equals(o)) return false;

        Eq eq = (Eq) o;

        return operator == eq.operator;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (operator ? 1 : 0);
        return result;
    }
}