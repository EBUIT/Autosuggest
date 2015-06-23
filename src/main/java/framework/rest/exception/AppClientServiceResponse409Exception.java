package framework.rest.exception;

import javax.ws.rs.core.Response;

/**
 * Created by laurent on 15.01.2015.
 */
public class AppClientServiceResponse409Exception extends AppClientServiceResponseException {


    public AppClientServiceResponse409Exception(String message) {
        super(message, 409);
    }

    public AppClientServiceResponse409Exception(String message, Exception e) {
        super(message, 409);
        initCause(e);
    }

    public AppClientServiceResponse409Exception(String message, Response response, Exception e) {
        super(message, response, e);
    }
}
