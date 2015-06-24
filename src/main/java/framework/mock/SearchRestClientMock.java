package framework.mock;

import framework.rest.task.PostListRestClient;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class SearchRestClientMock<R>  extends PostListRestClient<R> {

    @Value("${server.rootUrl}")
    public static String SEARCH ;

    public SearchRestClientMock() {

    }

    public SearchRestClientMock(Class<List<R>> entityClass) {
        super(entityClass);
        this.urlRelative = SEARCH;
    }

}
