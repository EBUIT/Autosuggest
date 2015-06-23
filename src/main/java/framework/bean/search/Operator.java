package framework.bean.search;

import java.util.List;

/**
 * Created by chardonnereau on 23.03.2015.
 */
public class Operator extends Term {
    private List<Term> terms;


    Operator(TermType termType, List<Term> terms) {
        super(null, null, termType);
        this.terms = terms;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Operator)) return false;
        if (!super.equals(o)) return false;

        Operator operator = (Operator) o;

        return !(terms != null ? !terms.equals(operator.terms) : operator.terms != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (terms != null ? terms.hashCode() : 0);
        return result;
    }
}
