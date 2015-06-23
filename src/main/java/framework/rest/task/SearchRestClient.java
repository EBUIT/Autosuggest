package framework.rest.task;

import java.util.List;

/**
 * Created by laurent on 03.02.2015.
 */
public class SearchRestClient<R> extends PostListRestClient<R> {

    public static final String SEARCH = "/framework/search";
    public SearchRestClient() {

    }

    public SearchRestClient(Class<List<R>> entityClass) {
        super(entityClass);
        this.urlRelative = SEARCH;
    }

}
