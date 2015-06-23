package framework.rest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import java.util.List;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Helper to create {@link WebTarget}
 */
@Service
public class WebTargetHelperService {

    private static final Logger LOG = getLogger(WebTargetHelperService.class);


    /**
     * server root url, example : http://localhost:8080/neos
     */
    @Value("${server.rootUrl}")
    private String rootUrl;


    /**
     * @param relativeUrlResource : url er
     * @param urlRelative
     * @param param
     * @return
     */
    public WebTarget createClient(String relativeUrlResource, String urlRelative, RestClientParameters param) {
        // load framework.rest client
        Client client = javax.ws.rs.client.ClientBuilder.newClient();
        // init client path
        WebTarget webTarget = client.target(rootUrl + StringUtils.defaultString(relativeUrlResource));


        //add url path
        if (urlRelative != null) {
            webTarget = webTarget.path(urlRelative);
        }
        if (param != null) {
            //add query param
            for (Map.Entry<String, Object> entry : param.getQueryParams().entrySet()) {
                Object value = entry.getValue();
                if(value instanceof List){
                    // Convert List parameter to Array to allow correct serialization
                    Object[] array = ((List) value).toArray();
                    webTarget = webTarget.queryParam(entry.getKey(), array);
                }else {
                    webTarget = webTarget.queryParam(entry.getKey(), value);
                }
            }

            //resolve url params
            webTarget = webTarget.resolveTemplates(param.getUrlParams());
        }
        LOG.debug("RestClient.webTarget created url=" + webTarget.getUri());
        return webTarget;
    }


}
