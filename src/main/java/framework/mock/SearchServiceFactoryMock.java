package framework.mock;

import framework.rest.*;
import framework.rest.task.SearchRestClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Generic Factory for create a Search Service.
 */
@Service
public class SearchServiceFactoryMock<T> {

    @Autowired
    private RestServiceFactory restServiceFactory;

    @Autowired
    private JsonConverterService jsonConverterService;

    @Autowired
    private WebTargetHelperService webTargetHelperService;

    @Autowired
    private ProcessResponse<List<T>> processResponse;

    @Autowired
    private RestUrlFacade restUrlFacade;

    /**
     * @param bean - Bean Type used to the creation of the framework.service.
     */
    private SearchRestClient searchRestClient(Class<List<T>> bean) {
        SearchRestClient abstractRestClient = new SearchRestClient(bean);

        abstractRestClient.setJsonConverterService(jsonConverterService);
        abstractRestClient.setProcessResponse(processResponse);
        abstractRestClient.setWebTargetHelperService(webTargetHelperService);
        abstractRestClient.setBaseUrl(restUrlFacade.getUrl(bean));

        return abstractRestClient;
    }

    /**
     * @param bean - Bean Type used to the creation of the framework.service.
     */
    public SearchServiceMock searchService(Class<List<T>> bean) {
        SearchRestClient searchRestClient = searchRestClient(bean);
        return new SearchServiceMock(searchRestClient, restServiceFactory);
    }
}
