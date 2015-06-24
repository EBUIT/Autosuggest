package sample.mockserver;

import org.mockserver.mock.action.ExpectationCallback;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.HttpStatusCode;

import static org.mockserver.model.HttpResponse.response;

/**
 * Created by metairie on 24-Jun-15.
 */
public class AutosuggestMockCallback implements ExpectationCallback {

    public AutosuggestMockCallback() {

    }

    public static HttpResponse httpResponse = response()
            .withStatusCode(
                    HttpStatusCode.OK_200.code()
            ).withBody(MockDatas.loadProfessionBean());

    @Override
    public HttpResponse handle(HttpRequest httpRequest) {
        return httpResponse;
    }

}