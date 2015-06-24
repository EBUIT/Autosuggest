package sample.mockserver;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;

import java.util.concurrent.TimeUnit;

import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpCallback.callback;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * Created by metairie on 24-Jun-15.
 */
public class Expectation {
    public enum SCENARIO {
        TEST,
        MOCK_1COL,
        MOCK_2COLS,
        REAL
    }

    public static void start(SCENARIO scenario) {
        switch (scenario) {
            case TEST:
                new MockServerClient("localhost", 8080)
                        .when(
                                request()
                                        .withMethod("POST")
                                        .withPath("/rest/profession/framework/search")
                                , unlimited()
                        )
                        .respond(
                                response()
                                        .withStatusCode(200)
                                        .withBody("[{ \"id\": 1, \"code\":\"PR1\", \"name\": \"Swimming\" }]")
                                        .withDelay(new Delay(TimeUnit.SECONDS, 1))
                        );
                break;
            case MOCK_1COL:
                new MockServerClient("localhost", 8080)
                        .when(
                                request()
                                        .withMethod("POST")
                                        .withPath("/rest/profession/framework/search")
                                , unlimited()
                        )
                        .callback(
                                callback()
                                        .withCallbackClass("sample.mockserver.AutosuggestMockCallback")
                        );
                break;
            case MOCK_2COLS:
            case REAL:
            default:
                new MockServerClient("localhost", 8080)
                        .when(
                                request()
                                        .withMethod("POST")
                                        .withPath("/rest/profession/framework/search")
                                , unlimited()
                        )
                        .callback(
                                callback()
                                        .withCallbackClass("sample.mockserver.AutosuggestMockCallback")
                        );
                break;
        }
    }

}
