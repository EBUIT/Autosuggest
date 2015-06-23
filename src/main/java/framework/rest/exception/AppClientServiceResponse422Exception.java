package framework.rest.exception;

import framework.exception.AppRuntimeException;
import framework.validation.ValidationErrorBean;

import javax.ws.rs.core.Response;

/**
 * Created by laurent on 15.01.2015.
 */
public class AppClientServiceResponse422Exception extends AppClientServiceResponseException {


    private ValidationErrorBean validationErrorBean;

    public AppClientServiceResponse422Exception(String message, ValidationErrorBean validationErrorBean) {
        super(message);
        this.validationErrorBean = validationErrorBean;
    }

    public AppClientServiceResponse422Exception(String s, Response response, AppRuntimeException e) {
        super(s, response, e);
    }

    public ValidationErrorBean getValidationErrorBean() {
        return validationErrorBean;
    }


}
