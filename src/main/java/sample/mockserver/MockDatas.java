package sample.mockserver;

import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import sample.combobox.*;

import java.util.Arrays;
import java.util.List;

/**
 * Created by metairie on 24-Jun-15.
 */
public class MockDatas {

    public static String loadLocationBean() {
        // data for Location
        return "[" +
                "{ \"id\": 1, \"code\":\"LO1\", \"name\": \"Swimming\" }," +
                "{ \"id\": 2, \"code\":\"LO2\", \"name\": \"Poland\" }," +
                "{ \"id\": 3, \"code\":\"LO3\", \"name\": \"Forest\" }," +
                "{ \"id\": 4, \"code\":\"LO4\", \"name\": \"Office\" }," +
                "{ \"id\": 5, \"code\":\"LO5\", \"name\": \"Swimming pool\" }," +
                "{ \"id\": 6, \"code\":\"LO6\", \"name\": \"Tribune\" }," +
                "{ \"id\": 7, \"code\":\"LO7\", \"name\": \"Office\" }," +
                "{ \"id\": 8, \"code\":\"LO8\", \"name\": \"Garden\" }" +
                "]";
    }

    public static String loadProfessionBean() {
        // data for Profession
        return "[" +
                "{ \"id\": 1, \"code\":\"PR1\", \"name\": \"Swimming\" }," +
                "{ \"id\": 2, \"code\":\"PR2\", \"name\": \"Politic\" }," +
                "{ \"id\": 3, \"code\":\"PR3\", \"name\": \"Poet\" }," +
                "{ \"id\": 4, \"code\":\"PR4\", \"name\": \"Podiatrist\" }," +
                "{ \"id\": 5, \"code\":\"PR5\", \"name\": \"Swimmer\" }," +
                "{ \"id\": 6, \"code\":\"PR6\", \"name\": \"Spokesman\" }," +
                "{ \"id\": 7, \"code\":\"PR7\", \"name\": \"Developer\" }," +
                "{ \"id\": 8, \"code\":\"PR8\", \"name\": \"Gardener\" }" +
                "]";
    }

    public static List<KeyValueStringLabel> loadLocation() {
        // data for Location
        KeyValueString lb1 = new KeyValueStringImpl("LO1", "Point of View");
        KeyValueString lb2 = new KeyValueStringImpl("LO2", "Poland");
        KeyValueString lb3 = new KeyValueStringImpl("LO3", "Forest");
        KeyValueString lb4 = new KeyValueStringImpl("LO4", "Office");
        KeyValueString lb5 = new KeyValueStringImpl("LO5", "Swimming pool");
        KeyValueString lb6 = new KeyValueStringImpl("LO6", "Tribune");
        KeyValueString lb7 = new KeyValueStringImpl("LO7", "Office");
        KeyValueString lb8 = new KeyValueStringImpl("LO8", "Garden");
        return Arrays.asList(lb1, lb2, lb3, lb4, lb5, lb6, lb7, lb8);
    }

    public static List<KeyValueTextFlow> loadLocationTextFlow() {
        // data for Location
        String family = "Helvetica";
        double size = 20;
        Text text1 = new Text("Po");
        text1.setFont(Font.font(family, FontWeight.BOLD, size));
        Text text2 = new Text("int of view");
        text2.setFont(Font.font(family, size));

        KeyValueTextFlow lb1 = new KeyValueTextFlowImpl("LO1", new TextFlow(text1, text2));
        KeyValueTextFlow lb2 = new KeyValueTextFlowImpl("LO2", new TextFlow(new Text("Poland")));
        KeyValueTextFlow lb3 = new KeyValueTextFlowImpl("LO3", new TextFlow(new Text("Forest")));
        KeyValueTextFlow lb4 = new KeyValueTextFlowImpl("LO4", new TextFlow(new Text("Office")));
        KeyValueTextFlow lb5 = new KeyValueTextFlowImpl("LO5", new TextFlow(new Text("Swimming pool")));
        KeyValueTextFlow lb6 = new KeyValueTextFlowImpl("LO6", new TextFlow(new Text("Tribune")));
        KeyValueTextFlow lb7 = new KeyValueTextFlowImpl("LO7", new TextFlow(new Text("Office")));
        KeyValueTextFlow lb8 = new KeyValueTextFlowImpl("LO8", new TextFlow(new Text("Garden")));
        return Arrays.asList(lb1, lb2, lb3, lb4, lb5, lb6, lb7, lb8);
    }

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
}
