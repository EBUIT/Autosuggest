package framework.exception;

/**
 * Created by laurent on 15.01.2015.
 */
public class AppRuntimeException extends RuntimeException {

    public AppRuntimeException(Exception e) {
        super(e);
    }

    public AppRuntimeException(String message) {
        super(message);
    }
}
