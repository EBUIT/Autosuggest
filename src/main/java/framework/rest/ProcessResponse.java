package framework.rest;

import framework.rest.exception.AppClientServiceResponse409Exception;
import framework.rest.exception.AppClientServiceResponse422Exception;
import framework.rest.exception.AppClientServiceResponseException;
import framework.validation.ErrorField;
import framework.validation.FieldProperty;
import framework.validation.ValidationErrorBean;
import com.fasterxml.jackson.core.type.TypeReference;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by laurent on 02.02.2015.
 */
@Service
public class ProcessResponse<T> {

    private static final Logger LOG = getLogger(ProcessResponse.class);

    @Autowired
    @Qualifier(value = "messageSourceUtil")
    private MessageSource messageSource;

    /**
     * Jackson converter framework.service
     */
    @Autowired
    private JsonConverterService jsonConverterService;

    public T processResponse(Response response, T bean) {
        switch (response.getStatus()) {
            case 200: {
                return bean;
            }
            case 422: {
                //framework.validation error
                LOG.info("Business exception=" + response.getStatus());
                String message = response.readEntity(String.class);
                LOG.debug(message);
                ValidationErrorBean validation;
                try {
                    validation = jsonConverterService.readValue(message, new TypeReference<ValidationErrorBean>() {
                    });
                } catch (Exception e) {
                    //serialisation failed, it's not a ValidationErrorBean (should not occured?)
                    throw new AppClientServiceResponseException(messageSource.getMessage("error.response422", null, null), response, e);
                }
                throw new AppClientServiceResponse422Exception(messageSource.getMessage("error.response422Generic", null, null), validation);
            }
            case 409: {
                //concurrent access
                LOG.info("Concurrent access detected");
                String message = response.readEntity(String.class);
                LOG.debug(message);
                final ErrorField errorField;
                try {
                    ValidationErrorBean validation = jsonConverterService.readValue(message, new TypeReference<ValidationErrorBean>() {
                    });
                    //read information about concurrent access exception (i.e :last user saved)
                    final FieldProperty fieldProperty = validation.getErrors().stream().findFirst().get();
                    errorField = fieldProperty.getMessages().stream().findFirst().get();
                } catch (Exception e) {
                    //serialisation failed, it's not a ValidationErrorBean
                    throw new AppClientServiceResponse409Exception(messageSource.getMessage("error.response409", null, null), response, e);
                }
                throw new AppClientServiceResponse409Exception(errorField.getDescription());
            }
            default: {
                LOG.error("Error response.status=" + response.getStatus());
                throw new AppClientServiceResponseException(response);
            }
        }

    }

    /**
     * If status != 200 send exception
     *
     * @param response
     * @throws AppClientServiceResponseException
     */
    public void checkResponseStatusOK(Response response) throws AppClientServiceResponseException {
        if (response.getStatus() != 200) {
            LOG.error("Error response.status=" + response.getStatus());
            throw new AppClientServiceResponseException(response);
        }
    }
}
