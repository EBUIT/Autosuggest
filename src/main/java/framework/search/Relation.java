package framework.search;

import org.apache.commons.lang3.text.WordUtils;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Join allow the association between 2 entities.<br/>
 * It's possible to add a filter on the joined elements.
 */
public class Relation extends SearchCriteriaNode {

    String type;
    String relationEntity;
    List<Term> terms;


    Relation(RelationType relationType, String relationEntity, Term... terms) {
        this(relationType, relationEntity, newArrayList(terms));
    }

    Relation(RelationType relationType, String relationEntity, List<Term> terms) {
        this.type = WordUtils.capitalizeFully(relationType.name());
        this.relationEntity = relationEntity;
        this.terms = newArrayList(terms);
    }

    public List<Term> getTerms() {
        return terms;
    }

    void setTerms(List<Term> terms) {
        this.terms = terms;
    }

    public String getRelationEntity() {
        return relationEntity;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(!(o instanceof Relation)) {
            return false;
        }

        Relation relation = (Relation) o;

        if(relationEntity != null ? !relationEntity.equals(relation.relationEntity) : relation.relationEntity != null) {
            return false;
        }
        return !(terms != null ? !terms.equals(relation.terms) : relation.terms != null);

    }

    public int hashCode() {
        int result = relationEntity != null ? relationEntity.hashCode() : 0;
        result = 31 * result + (terms != null ? terms.hashCode() : 0);
        return result;
    }

    public enum RelationType {
        JOIN,
        FETCH
    }
}
