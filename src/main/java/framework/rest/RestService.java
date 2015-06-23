package framework.rest;

import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * JavaFx Service to call {@link RestClientFunction}
 * Created by laurent on 05.02.2015.
 */
public class RestService<V> extends Service<V> {

    private RestClientFunction<V> restClient;
    private RestClientParameters restClientParameters;

    /**
     * Should not be used directly. Use {@link RestServiceFactory}
     *
     * @param restClient
     * @param restClientParameters
     */
    public RestService(RestClientFunction<V> restClient, RestClientParameters restClientParameters) {
        this.restClient = restClient;
        this.restClientParameters = restClientParameters;
    }

    @Override
    protected Task<V> createTask() {
        return new Task<V>() {
            @Override
            protected V call() throws Exception {
                return restClient.apply(restClientParameters);

            }
        };
    }

    public RestClientFunction<V> getRestClient() {
        return restClient;
    }

    public void setRestClient(RestClientFunction<V> restClient) {
        this.restClient = restClient;
    }

    public RestClientParameters getRestClientParameters() {
        return restClientParameters;
    }

    public void setRestClientParameters(RestClientParameters restClientParameters) {
        this.restClientParameters = restClientParameters;
    }
}
