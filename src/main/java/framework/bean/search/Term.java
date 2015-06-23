package framework.bean.search;

import org.apache.commons.lang3.text.WordUtils;

/**
 * Created by chardonnereau on 18.03.2015.
 */
public class Term extends SearchCriteriaNode {
    private String identifier;
    private Object value;
    private String type;

    /**
     * The term is defined by an {@code identifier}
     *
     * @param identifier - entity identifier
     * @param value      - term's value
     * @param like
     */
    Term(String identifier, Object value, TermType like) {
        this.value = value;
        this.identifier = identifier;
        this.type = WordUtils.capitalizeFully(like.name());
    }

    /**
     * The term is defined by an {@code identifier}
     *
     * @param identifier - entity identifier
     * @param value      - term's value
     * @param like
     */
    Term(String identifier, Object value, TermType like, boolean operator) {
        this.value = value;
        this.identifier = identifier;
        this.type = WordUtils.capitalizeFully(like.name());
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getType() {
        return type;
    }

    public Object getValue() {
        return value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Term)) return false;

        Term term = (Term) o;

        if (identifier != null ? !identifier.equals(term.identifier) : term.identifier != null) return false;
        if (type != null ? !type.equals(term.type) : term.type != null) return false;
        return !(value != null ? !value.equals(term.value) : term.value != null);

    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
