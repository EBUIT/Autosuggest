package framework.rest;

import framework.service.callback.CallbackService;
import framework.service.callback.LoggerCallbackService;
import javafx.concurrent.Service;
import javafx.scene.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

@org.springframework.stereotype.Service
public class RestServiceFactory {


    @Resource(name = "restExecutor")
    private Executor executor;


    @Autowired
    private RecordCallbackOnService recordCallback;


    /**
     * Create an instance of RestServiceBuilder with no default value
     *
     * @param restClient
     * @param <T>
     * @return
     */
    public <T> RestServiceBuilder<T> newInstance(RestClientFunction<T> restClient) {
        final RestServiceBuilder restServiceBuilder = new RestServiceBuilder(restClient);
        return restServiceBuilder;
    }

    /**
     * Create an instance of RestServiceBuilder with default CallBack :
     * <ul>
     * <li>{@link ch.ebu.neos.client.util.service.callback.ExceptionHandlerCallbackService}</li>
     * <li>{@link ch.ebu.neos.client.util.service.callback.LoggerCallbackService}</li>
     * <li>{@link ch.ebu.neos.client.util.service.callback.DisableGuiCallbackService}</li>
     * <li>{@link ch.ebu.neos.client.util.service.callback.ValidationCallbackService}</li>
     * <p/>
     * </ul
     *
     * @param restClient
     * @param node       Node to set to disable during process and node root to find framework.ui control in error
     * @param <T>        the type of the result of the framework.service
     * @return
     */
    public <T> RestServiceBuilder<T> createServiceBuilder(RestClientFunction<T> restClient, Node node) {
        final RestServiceBuilder restServiceBuilder = newInstance(restClient);
        restServiceBuilder.setExecutor(executor);

        // logger
        restServiceBuilder.addCallBack(new LoggerCallbackService());

        return restServiceBuilder;
    }

    /**
     * Create an instance of RestServiceBuilder with default CallBack :
     * <ul>
     * <li>{@link ch.ebu.neos.client.util.service.callback.ExceptionHandlerCallbackService}</li>
     * <li>{@link ch.ebu.neos.client.util.service.callback.LoggerCallbackService}</li>
     * <li>{@link ch.ebu.neos.client.util.service.callback.DisableGuiCallbackService}</li>
     * <li>{@link ch.ebu.neos.client.util.service.callback.ValidationCallbackService}</li>
     * <p/>
     * </ul
     *
     * @param restClient
     * @param parameters parameters used by the restClient
     * @param node       Node to set to disable during process and node root to find framework.ui control in error
     * @param <T>        the type of the result of the framework.service
     * @return
     */
    public <T> Service<T> createService(RestClientFunction<T> restClient, RestClientParameters parameters, Node node) {
        RestServiceBuilder builder = createServiceBuilder(restClient, node);
        builder.setRestClientParameters(parameters);
        return builder.build();
    }

    /**
     * @param restClient
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> Service<T> createService(RestClientFunction<T> restClient, Map<String, Object> parameters) {
        RestClientParameters restClientParameters = new RestClientParameters();
        for (Map.Entry<String, Object> parameter : parameters.entrySet()) {
            restClientParameters.addUrlParam(parameter.getKey(), parameter.getValue());
        }
        return createService(restClient, restClientParameters, null);
    }

    public <T> Service<T> createService(RestClientFunction<T> restClient, RestClientParameters parameters) {
        RestServiceBuilder builder = createServiceBuilder(restClient, null);
        builder.setRestClientParameters(parameters);
        return builder.build();
    }

    /**
     * Service to get a full resource object
     *
     * @param id
     * @return
     */
    public <T> Service<T> createServiceWithId(RestClientFunction<T> restClient, Long id) {
        final RestClientParameters restClientParameters = new RestClientParameters();
        restClientParameters.setIdUrlParam(id);
        return createService(restClient, restClientParameters, null);
    }

    /**
     * Create a Service from Object[][]
     *
     * @param restClient
     * @param parameters
     * @param <T>
     * @return
     */
    public <T> Service<T> createService(RestClientFunction<T> restClient, Object[][] parameters) {
        Assert.notNull(parameters);
        RestClientParameters restClientParameters = new RestClientParameters();
        for (Object[] parameter : parameters) {
            Assert.isTrue(parameter.length == 2);
            if (parameter[1] != null) {
                restClientParameters.addUrlParam((String) parameter[0], parameter[1]);
            }
        }
        return createService(restClient, restClientParameters, null);
    }

    /**
     * RestServiceBuilder
     *
     * @param <T>
     */
    public class RestServiceBuilder<T> {
        private Executor executor;
        private RestClientFunction<T> restClient;
        private RestClientParameters restClientParameters = new RestClientParameters();
        private List<CallbackService> listCallback = new ArrayList<>();

        public RestServiceBuilder(RestClientFunction<T> restClient) {
            this.restClient = restClient;
        }

        public void addCallBack(CallbackService callback) {
            listCallback.add(callback);
        }

        public void setExecutor(Executor executor) {
            this.executor = executor;
        }

        public RestServiceBuilder setRestClientParameters(RestClientParameters restClientParameters) {
            this.restClientParameters = restClientParameters;
            return this;
        }


        public Service build() {
            Service<T> service = new RestService(restClient, restClientParameters);
            for (CallbackService callback : listCallback) {
                recordCallback.addCallback(service, callback);
            }
            return service;
        }
    }
}
