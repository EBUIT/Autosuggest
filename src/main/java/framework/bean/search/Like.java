package framework.bean.search;

/**
 * Created by chardonnereau on 23.03.2015.
 */
public class Like extends Term {

    private LikeType likeType;
    /**
     * negation : if true do a Like else a notLike
     */
    private boolean operator;

    /**
     * The term is defined by an {@code identifier}
     *
     * @param identifier - entity identifier
     * @param value      - term's value
     * @param like
     * @param operator like or notLike
     */
    Like(String identifier, Object value, TermType like, LikeType likeType, boolean operator) {
        super(identifier, value, like);
        this.likeType = likeType;
        this.operator = operator;
    }

    public LikeType getLikeType() {
        return likeType;
    }

    public boolean isOperator() {
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Like)) return false;
        if (!super.equals(o)) return false;

        Like like = (Like) o;

        if (operator != like.operator) return false;
        return likeType == like.likeType;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (likeType != null ? likeType.hashCode() : 0);
        result = 31 * result + (operator ? 1 : 0);
        return result;
    }

    public enum LikeType {
        CONTAINS,
        BEGIN,
        END
    }
}
