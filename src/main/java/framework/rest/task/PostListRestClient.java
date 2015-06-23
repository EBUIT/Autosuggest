package framework.rest.task;

import framework.rest.RestClientParameters;
import com.fasterxml.jackson.databind.type.CollectionType;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by metairie on 23-Mar-15.
 *
 * Do GET and return List<T>
 *
 * @param
 * @return List of T objects
 */
public class PostListRestClient<T>  extends AbstractRestClient<List<T>> {

    public PostListRestClient() {
    }

    public PostListRestClient(Class<List<T>> entityClass) {
        super(entityClass);
    }

    /**
     * Do POST and return List<T>
     *
     * @param parameters
     * @return
     */
    @Override
    public List<T> apply(RestClientParameters parameters) {
        final Object data = parameters.getBody();

        Entity<String> entity = convertToString(data);
        final Response response = createWebTarget(parameters).post(entity);
        processResponse.processResponse(response, null);

        if (response.getStatus() == 200) {
            final CollectionType collectionType = TYPE_FACTORY.constructCollectionType(ArrayList.class, entityClass);
            Object json = jsonConverterService.readValue(response.readEntity(String.class), collectionType);
            return convertToBean(json);
        }
        return  null;
    }

}
