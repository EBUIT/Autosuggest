package sample.mockserver;

import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by metairie on 24-Jun-15.
 */
public class AutosuggestMockCallback implements ExpectationCallback {
    private final static Logger LOG = LoggerFactory.getLogger(AutosuggestMockCallback.class);

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        String json = MockDatas.loadProfessionBean(httpRequest.getBody());
        LOG.debug("json POSTed by client : " + json);
        HttpResponse hr = HttpResponse.response().withStatusCode(HttpStatusCode.OK_200.code()).withBody(json);
        LOG.debug("HttpResponse : " + hr.getBodyAsString());
        return hr;
    }
}