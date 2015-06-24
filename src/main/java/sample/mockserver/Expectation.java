package sample.mockserver;

import org.mockserver.client.server.MockServerClient;
import org.mockserver.model.Delay;

import java.util.concurrent.TimeUnit;

import static org.mockserver.matchers.Times.unlimited;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * Created by metairie on 24-Jun-15.
 */
public class Expectation {
    public static void start() {
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
    }
}
