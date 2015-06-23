package framework.rest;

import framework.exception.AppRuntimeException;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.List;

/**
 * Jackson converter framework.service
 * <p/>
 * Created by laurent on 15.01.2015.
 */
@Service
public class JsonConverterService {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static ObjectMapper getMapper() {
        return mapper;
    }

    @PostConstruct
    private void initDefaultObjectMapper() {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_SELF_REFERENCES, true);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        mapper.getDeserializationConfig()
                .with(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
                .with(DeserializationFeature.USE_BIG_INTEGER_FOR_INTS);

        mapper.registerModule(new JSR310Module());

    }


    public String toJson(Object object) {
        try {
            return mapper.writeValueAsString(object);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }

    public Entity<String> toJsonEntity(Object object) {
        String json = toJson(object);
        return Entity.entity(json, MediaType.APPLICATION_JSON);
    }

    /**
     * Convert Json to java Object
     *
     * @param json
     * @param typeReference Expected type to convert : <T>
     * @param <T>
     * @return
     */
    public <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return mapper.readValue(json, typeReference);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }

    public <T> T readValue(String json, JavaType javaType) {
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }
    }

    public <T> List<T> readListValue(String json, JavaType javaType) {
        try {
            return mapper.readValue(json, javaType);
        } catch (IOException e) {
            throw new AppRuntimeException(e);
        }

    }
}
