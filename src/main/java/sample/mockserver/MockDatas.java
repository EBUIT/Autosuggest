package sample.mockserver;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import framework.bean.search.SearchCriteria;
import javafx.collections.FXCollections;
import org.mockserver.model.Body;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.combobox.KeyValueString;
import sample.combobox.KeyValueStringImpl;
import sample.combobox.KeyValueStringLabel;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

/**
 * Created by metairie on 24-Jun-15.
 */
public class MockDatas {
    private final static Logger LOG = LoggerFactory.getLogger(MockDatas.class);

    // --------------------------------------------
    //  dynamic values
    // --------------------------------------------

    /**
     * get Dynamic values
     *
     * @param body
     * @return
     */
    public static String loadLocationBean(Body body) {
        if (body == null || body.getRawBytes().length <= 0) {
            return null;
        }

        // body is a searchCriteria
        CustomObjectMapper mapper = new CustomObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
        mapper.enable(DeserializationFeature.ACCEPT_EMPTY_ARRAY_AS_NULL_OBJECT);

        SearchCriteria sc = SearchCriteria.of();
        try {
            String toBeConverted = new String(body.getRawBytes(), "UTF-8");
            LOG.debug("convert JSON to Bean : {}", toBeConverted);
            sc = mapper.readValue(toBeConverted, SearchCriteria.class);
        } catch (IOException e) {
            LOG.debug("Error in the mockdatas ! Michael JSON converter failed.");
            e.printStackTrace();
        }
        final String code = (sc.getTerms().get(0)).getValue().toString();
        return toJson(new MockDatas().loadLocation().stream().filter(kv -> kv.getValue().toUpperCase().contains((code.toUpperCase()))));
    }

    private static String toJson(Stream s) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        Object[] newList = s.toArray();
        for (Object kv : newList) {
            sb.append("{\"id\":" + Integer.toString(new Random().nextInt()) + ", \"code\":\"" + ((KeyValueString) kv).getKey() + "\", \"name\": \"" + ((KeyValueString) kv).getValue() + "\"}," +
                    "\n");
        }
        if (sb.length() > 1) {
            sb.delete(sb.length() - 2, sb.length());
        }
        sb.append("]");
        LOG.debug(" json = " + sb.toString());
        return sb.toString();
    }

    // --------------------------------------------
    //  static
    // --------------------------------------------

    // ----------
    // LOCATION
    // ----------
    public static String loadLocationJson() {
        // data for Location
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("{ \"id\": 1, \"code\":\"LO1\", \"name\": \"Point of View\" },");
        sb.append("{ \"id\": 2, \"code\":\"LO2\", \"name\": \"Poland\" },");
        sb.append("{ \"id\": 3, \"code\":\"LO3\", \"name\": \"Forest\" },");
        sb.append("{ \"id\": 4, \"code\":\"LO4\", \"name\": \"Office\" },");
        sb.append("{ \"id\": 5, \"code\":\"LO5\", \"name\": \"Swimming pool\" },");
        sb.append("{ \"id\": 6, \"code\":\"LO6\", \"name\": \"Tribune\" },");
        sb.append("{ \"id\": 7, \"code\":\"LO7\", \"name\": \"Office\" },");
        sb.append("{ \"id\": 8, \"code\":\"LO8\", \"name\": \"Garden\" }");
        sb.append("]");
        return sb.toString();
    }

    public List<KeyValueString> loadLocation() {
        // data for Location
        KeyValueString lb1 = new KeyValueStringImpl("LO1" , "Point of View");
        KeyValueString lb2 = new KeyValueStringImpl("LO2" , "Poland");
        KeyValueString lb3 = new KeyValueStringImpl("LO3" , "Forest");
        KeyValueString lb4 = new KeyValueStringImpl("LO4" , "Office");
        KeyValueString lb5 = new KeyValueStringImpl("LO5" , "Swimming pool");
        KeyValueString lb6 = new KeyValueStringImpl("LO6" , "Tribune");
        KeyValueString lb7 = new KeyValueStringImpl("LO7" , "Office");
        KeyValueString lb8 = new KeyValueStringImpl("LO8" , "Garden");
        return Arrays.asList(lb1, lb2, lb3, lb4, lb5, lb6, lb7, lb8);
    }

    public static Collection<?> loadLocationStrings() {
        return FXCollections.observableArrayList("Point of View", "Poland", "Forest", "Office", "Swimming pool", "Tribune", "Office", "Garden");
    }

    // ----------
    // PROFESSION
    // ----------
    public static List<KeyValueStringLabel> loadProfession() {
        // data for Profession
        KeyValueString pb1 = new KeyValueStringImpl("PR1", "Photographer");
        KeyValueString pb2 = new KeyValueStringImpl("PR2", "Politic");
        KeyValueString pb3 = new KeyValueStringImpl("PR3", "Poet");
        KeyValueString pb4 = new KeyValueStringImpl("PR4", "Podiatrist");
        KeyValueString pb5 = new KeyValueStringImpl("PR5", "Swimmer");
        KeyValueString pb6 = new KeyValueStringImpl("PR6", "Spokesman");
        KeyValueString pb7 = new KeyValueStringImpl("PR7", "Developer");
        KeyValueString pb8 = new KeyValueStringImpl("PR8", "Gardener");
        return Arrays.asList(pb1, pb2, pb3, pb4, pb5, pb6, pb7, pb8);
    }

    public static String loadProfessionJson() {
        // data for Profession
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        sb.append("{ \"id\": 1, \"code\":\"PR1\", \"name\": \"Photographer\" },");
        sb.append("{ \"id\": 2, \"code\":\"PR2\", \"name\": \"Politic\" },");
        sb.append("{ \"id\": 3, \"code\":\"PR3\", \"name\": \"Poet\" },");
        sb.append("{ \"id\": 4, \"code\":\"PR4\", \"name\": \"Podiatrist\" },");
        sb.append("{ \"id\": 5, \"code\":\"PR5\", \"name\": \"Swimmer\" },");
        sb.append("{ \"id\": 6, \"code\":\"PR6\", \"name\": \"Spokesman\" },");
        sb.append("{ \"id\": 7, \"code\":\"PR7\", \"name\": \"Developer\" },");
        sb.append("{ \"id\": 8, \"code\":\"PR8\", \"name\": \"Gardener\" }");
        sb.append("]");
        return sb.toString();
    }
}
