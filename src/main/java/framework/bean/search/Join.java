package framework.bean.search;

import java.util.List;

/**
 * Created by chardonnereau on 11.05.2015.
 */
public class Join extends Relation {

    Relation subJoin;

    Join(String relationEntity) {
        super(RelationType.JOIN, relationEntity);
    }

    static Join join(String relationEntity, List<Term> terms) {
        Join join = new Join(relationEntity);
        join.setTerms(terms);
        return join;
    }

    static Join join(String relationEntity, Join subJoin) {
        Join join = new Join(relationEntity);
        join.setSubJoin(subJoin);
        return join;
    }

    public Relation getSubJoin() {
        return subJoin;
    }

    public void setSubJoin(Join subJoin) {
        this.subJoin = subJoin;
    }


}
