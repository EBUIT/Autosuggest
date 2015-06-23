package framework.rest.exception;

import javax.ws.rs.core.Response;

/**
 * Created by laurent on 15.01.2015.
 */
public class AppClientServiceResponseException extends RuntimeException {

    private int httpStatus = -1;

    public AppClientServiceResponseException(String message, Response response) {
        super(message);
        this.httpStatus = response.getStatus();
    }

    public AppClientServiceResponseException(String message, Response response, Exception e) {
        super(message, e);
        this.httpStatus = response.getStatus();
    }

    public AppClientServiceResponseException(String message, int httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public AppClientServiceResponseException(Response response) {
        super();
        this.httpStatus = response.getStatus();
    }

    public AppClientServiceResponseException(String message) {
        super(message);
    }

    public String toString() {
        return " message=" + getMessage() + " status=" + httpStatus;
    }

}
