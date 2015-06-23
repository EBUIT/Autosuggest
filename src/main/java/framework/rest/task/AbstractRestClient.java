package framework.rest.task;

import framework.bean.AbstractBeanConverter;

import com.fasterxml.jackson.databind.type.TypeFactory;
import com.google.common.base.Strings;
import framework.rest.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by laurent on 03.02.2015.
 */
public abstract class AbstractRestClient<T> implements RestClientFunction<T> {


    private static final Logger LOG = LoggerFactory.getLogger(AbstractRestClient.class);
    /**
     * Default type factory
     */
    protected final TypeFactory TYPE_FACTORY = TypeFactory.defaultInstance();
    public Map<String, Object> defaultQueryParams = new HashMap<>();
    @Autowired
    protected JsonConverterService jsonConverterService;
    @Autowired
    protected WebTargetHelperService webTargetHelperService;
    @Autowired
    protected ProcessResponse<T> processResponse;
    /**
     * Entity Class of {@link T}
     */
    protected Class<T> entityClass;
    /**
     * Base url of framework.service : REST base url resource. Example /organizations
     * Mus not be null
     */
    protected String baseUrl;
    /**
     * Url suffix : can contains placeholder. Example /children/{idChild}/items
     * Can be null
     */
    protected String urlRelative;
    @Autowired
    private RestUrlFacade restUrlFacade;
    private AbstractBeanConverter converter;

    /**
     * For framework.spring instantiation
     *
     * @param entityClass
     */
    public AbstractRestClient(Class<T> entityClass) {
        this.entityClass = entityClass;
    }


    @SuppressWarnings("unchecked")
    public AbstractRestClient() {
        this.entityClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void setDefaultQueryParams(Map<String, Object> defaultQueryParams) {
        this.defaultQueryParams = defaultQueryParams;
    }

    @Override
    abstract public T apply(RestClientParameters parameters);

    protected Invocation.Builder createWebTarget(RestClientParameters parameters) {
        RestClientParameters newParameters = parameters;

        if (newParameters == null) {
            newParameters = new RestClientParameters();
        }

        // add default url parameters
        if (this.defaultQueryParams != null && !this.defaultQueryParams.isEmpty()) {
            newParameters.getQueryParams().putAll(this.defaultQueryParams);
        }
        WebTarget client = webTargetHelperService.createClient(getBaseUrl(), urlRelative, newParameters);
        Invocation.Builder request = client.request(MediaType.APPLICATION_JSON);

        newParameters.getHeaderParams().forEach(request::header);

        return request;

    }

    private String getBaseUrl() {
        if(Strings.isNullOrEmpty(baseUrl)) {
            baseUrl = restUrlFacade.getUrl(entityClass);
        }
        return baseUrl;
    }

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public String getUrlRelative() {
        return urlRelative;
    }

    public void setUrlRelative(String urlRelative) {
        this.urlRelative = urlRelative;
    }

    public AbstractBeanConverter getConverter() {
        return converter;
    }

    public void setConverter(AbstractBeanConverter converter) {
        this.converter = converter;
    }

    public boolean containsConverter() {
        return converter != null;
    }

    Type getDestinationType() {
        if (containsConverter()) {
            return getConverter().getDestinationType();
        } else {
            return entityClass;
        }
    }

    Type getSourceType() {
        if (containsConverter()) {
            return getConverter().getDestinationType();
        } else {
            return entityClass;
        }
    }

    @SuppressWarnings("unchecked")
    T convertToBean(Object json) {
        if (containsConverter()) {
            return (T) getConverter().convertFrom(json);
        } else {
            return (T) json;
        }
    }

    @SuppressWarnings("unchecked")
    Entity<String> convertToString(Object object) {
        Object objectToConvert = null;
        if (containsConverter()) {
            objectToConvert = getConverter().convertTo(object);
        } else {
            objectToConvert = object;
        }
        String json = jsonConverterService.toJson(objectToConvert);
        LOG.debug("JSON : {}", json);
        return Entity.entity(json, MediaType.APPLICATION_JSON);
    }

    public void setJsonConverterService(JsonConverterService jsonConverterService) {
        this.jsonConverterService = jsonConverterService;
    }

    public void setProcessResponse(ProcessResponse<T> processResponse) {
        this.processResponse = processResponse;
    }

    public void setWebTargetHelperService(WebTargetHelperService webTargetHelperService) {
        this.webTargetHelperService = webTargetHelperService;
    }
}
