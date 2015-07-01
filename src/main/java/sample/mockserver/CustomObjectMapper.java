package sample.mockserver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import javax.annotation.PostConstruct;

/**
 * Created by metairie on 01-Jul-15.
 */
public class CustomObjectMapper extends ObjectMapper {
    @PostConstruct
    public void customConfiguration() {
        // Uses Enum.toString() for serialization of an Enum
        this.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        // Uses Enum.toString() for deserialization of an Enum
        this.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    }

}
