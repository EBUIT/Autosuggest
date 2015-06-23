package framework.rest;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.util.Assert.isTrue;

/**
 * Data used by RestClient
 * Created by laurent on 03.02.2015.
 */
public class RestClientParameters {

    /**
     * Query params to use for building the url
     * example : ...?param1=value1&foo=ok
     */
    public final Map<String, Object> queryParams = new HashMap<>();

    /**
     * Header parameters
     */
    public final Map<String, Object> headerParams = new HashMap<>();

    /**
     * url params to resolve the url template
     * example : With url = /foo/{id}/items the urlParams must contains an entry like this "id"->xxx
     */
    public final Map<String, Object> urlParams = new HashMap<>();

    /**
     * body will be serialize and send in the POST or PUT request
     */
    public Object body;

    /**
     * Create a new instance
     *
     * @return
     */
    public static RestClientParameters newInstance() {
        return new RestClientParameters();
    }


    public static RestClientParameters of(Object objectParameter) {
        RestClientParameters parameters = RestClientParameters.newInstance();
        parameters.body = objectParameter;
        return parameters;
    }

    /**
     * Add a query param , example : /xxx/yyy?param1=value1
     *
     * @param key
     * @param value
     * @return
     */
    public RestClientParameters addQueryParam(String key, Object value) {
        isTrue(key != null);
        queryParams.put(key, value);
        return this;
    }

    public RestClientParameters addHeaderParam(String key, Object value) {
        isTrue(key != null);
        headerParams.put(key, value);
        return this;
    }

    /**
     * Add a url param to resolve url template<br/>
     * Example : With url = /foo/{id}/items the urlParams must contains an entry like this "id"->xxx
     *
     * @param key
     * @param value
     * @return
     */
    public RestClientParameters addUrlParam(String key, Object value) {
        isTrue(key != null);
        isTrue(value != null);
        urlParams.put(key, value);
        return this;
    }

    /**
     * Resolve {id} in url template
     *
     * @param id : Use to resolve the url template. Example: /foo/{id}/items
     * @return
     */
    public RestClientParameters setIdUrlParam(Long id) {
        getUrlParams().put("id", id);
        return this;
    }

    public Map<String, Object> getHeaderParams() {
        return headerParams;
    }

    public Map<String, Object> getQueryParams() {
        return queryParams;
    }

    public Map<String, Object> getUrlParams() {
        return urlParams;
    }

    public Object getBody() {
        return body;
    }

    /**
     * Request body
     *
     * @param body
     * @return
     */
    public RestClientParameters setBody(Object body) {
        this.body = body;
        return this;
    }


}
