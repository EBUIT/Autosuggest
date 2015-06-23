package framework.bean.search;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Factory to build all elements usable by the searchCriteria.<br/>
 * <ul>
 * <li>Like</li>
 * <li>Join</li>
 * <li>Eq</li>
 * </ul>
 */
public class SearchElementFactory {

    public static Term notlike(String identifier, Object value, Like.LikeType likeType) {
        return new Like(identifier, value, TermType.LIKE, likeType, false);
    }

    public static Term like(String identifier, Object value, Like.LikeType likeType) {
        return new Like(identifier, value, TermType.LIKE, likeType, true);
    }

    public static Term like(String identifier, Object value, Like.LikeType likeType, boolean negation) {
        return new Like(identifier, value, TermType.LIKE, likeType, negation);
    }

    public static Term like(String identifier, Object value) {
        return new Like(identifier, value, TermType.LIKE, Like.LikeType.CONTAINS, true);
    }

    public static Term and(List<Term> terms) {
        return new Operator(TermType.AND, terms);
    }

    public static Term and(Term... terms) {
        return new Operator(TermType.AND, newArrayList(terms));
    }

    public static Term or(Term... terms) {
        return new Operator(TermType.OR, newArrayList(terms));
    }

    public static Term or(List<Term> terms) {
        return new Operator(TermType.OR, terms);
    }

    public static Term in(String identifier, List<String> values) {
        return new Term(identifier, values, TermType.IN);
    }

    public static Relation join(String identifier, Term... terms) {
        return Join.join(identifier, newArrayList(terms));
    }

    public static Relation join(String identifier, List<Term> terms) {
        return Join.join(identifier, terms);
    }

    public static Relation join(String identifier, Join subJoin) {
        return Join.join(identifier, subJoin);
    }

    public static Relation fetch(String identifier, Term... terms) {
        return new Relation(Relation.RelationType.FETCH, identifier, newArrayList(terms));
    }

    public static Relation fetch(String identifier, List<Term> terms) {
        return new Relation(Relation.RelationType.FETCH, identifier, terms);
    }

    public static void in() {
    }

    public static Term eq(String identifier, Object value) {
        return new Eq(identifier, value, TermType.EQ, true);
    }

    public static Term notEq(String identifier, Object value) {
        return new Eq(identifier, value, TermType.EQ, false);
    }

    public static Term eq(String identifier, Object value, boolean negation) {
        return new Eq(identifier, value, TermType.EQ, negation);
    }

    public static Term greater(String identifier, Object value) {
        return new Term(identifier, value, TermType.GREATER);
    }

    public static Term greaterOrEqual(String identifier, Object value) {
        return new Term(identifier, value, TermType.GREATER_OR);
    }

    public static Term less(String identifier, Object value) {
        return new Term(identifier, value, TermType.LESS);
    }

    public static Term lessOrEqual(String identifier, Object value) {
        return new Term(identifier, value, TermType.LESS_OR);
    }
}
