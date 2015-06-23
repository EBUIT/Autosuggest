package framework.service;

import framework.rest.RestClientParameters;
import framework.rest.RestServiceFactory;
import framework.rest.task.PostListRestClient;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * Create searchService
 * @param <T>
 */
public class SearchService<T> {

    @Autowired
    private RestServiceFactory restServiceFactory;

    //framework.bean to inject
    private PostListRestClient<T> postListRestClient;

    public SearchService(PostListRestClient<T> getListRestClient) {
        this.postListRestClient = getListRestClient;
    }

    public SearchService(PostListRestClient<T> getListRestClient, RestServiceFactory restServiceFactory) {
        this.postListRestClient = getListRestClient;
        this.restServiceFactory = restServiceFactory;
    }

    public PostListRestClient<T> getPostListRestClient() {
        return postListRestClient;
    }

    public Service search(Object searchCriteria, List<T> resultList) {
        final Service<T> service = search(searchCriteria);
        service.setOnSucceeded((WorkerStateEvent event) -> {
            final List<T> result = (List<T>) event.getSource().getValue();
            resultList.addAll(result);
        });
        return service;
    }

    public Service search(Object searchCriteria) {
        RestClientParameters restClientParameters = RestClientParameters.of(searchCriteria);
        final Service<? extends List<T>> service = restServiceFactory.createService(postListRestClient, restClientParameters);
        return service;
    }

}
