package sample;

import framework.bean.search.Like;
import framework.bean.search.SearchCriteria;
import framework.mock.SearchServiceFactoryMock;
import framework.mock.SearchServiceMock;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.util.StringConverter;
import sample.autosuggest.AutoSuggestSearchRestClientMock;
import sample.combobox.AutoSuggestKeyValueString;
import sample.combobox.KeyValueString;
import sample.combobox.KeyValueStringImpl;
import sample.combobox.KeyValueStringLabel;

import javax.annotation.Resource;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

import static framework.bean.search.SearchElementFactory.*;

public class Controller implements Initializable {

    @FXML
    Label dataLabelLocation, dataLabelProfession;
    @FXML
    AutoSuggestKeyValueString autosuggestLocation = new AutoSuggestKeyValueString();
    @FXML
    AutoSuggestKeyValueString autosuggestProfession = new AutoSuggestKeyValueString();
    @FXML
    private AutoSuggestSearchRestClientMock<ProfessionBean> autosuggestSearch;

    @Resource
    private SearchServiceFactoryMock searchServiceFactoryMock;

    private ObjectProperty<KeyValueStringLabel> dataLocationProperty = new SimpleObjectProperty<>();
    private ObjectProperty<KeyValueStringLabel> dataprofessionProperty = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // WARN this is a workaround
        // the controller instance creation done by FXMLLoader avoids to Autowired properties/methods bean
        Main.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);

        // datas
        final List<KeyValueStringLabel> itemsLocation = loadLocation();
        final List<KeyValueStringLabel> itemsProfession = loadProfession();

        // init sample.autosuggest
        autosuggestLocation.init(searchFunctionParam(itemsLocation), textFieldFormatter, labelItemFormatter);
        autosuggestProfession.init(searchFunctionParam(itemsProfession), textFieldFormatter, labelItemFormatter);
        updateGenericAutoSuggest(autosuggestSearch, searchServiceFactoryMock.searchService(ProfessionBean.class),
                t -> String.format("%s - %s", t.getCode().toString(), t.getName()), t -> String.format("%s - %s", t.getCode().toString(), t.getName()),
                "code", "name"
        );

        // bind with Labels
        bind();
    }

    public static void updateGenericAutoSuggest(
            AutoSuggestSearchRestClientMock<ProfessionBean> autoSuggest,
            SearchServiceMock<ProfessionBean> searchService,
            Function<ProfessionBean, String> txtField,
            Function<ProfessionBean, String> cellField,
            String code,
            String name) {
        autoSuggest.init(searchService, txtField, cellField,
                s -> SearchCriteria.of().likeBegin(code, s),
                s1 -> SearchCriteria.of().and(notlike(code, s1, Like.LikeType.BEGIN), or(like(code, s1), like(name, s1)))
        );
    }

    private void bind() {
        autosuggestLocation.valueProperty().bindBidirectional(dataLocationProperty);
        Bindings.bindBidirectional(dataLabelLocation.textProperty(), dataLocationProperty, new StringConverter<KeyValueStringLabel>() {
            @Override
            public String toString(KeyValueStringLabel object) {
                return object == null ? "No Value Selected" : object.getValue();
            }

            @Override
            public KeyValueString fromString(String string) {
                throw new UnsupportedOperationException("Converter not implemented");
            }
        });
        autosuggestProfession.valueProperty().bindBidirectional(dataprofessionProperty);
        Bindings.bindBidirectional(dataLabelProfession.textProperty(), dataprofessionProperty, new StringConverter<KeyValueStringLabel>() {
            @Override
            public String toString(KeyValueStringLabel object) {
                return object == null ? "No Value Selected" : object.getValue();
            }

            @Override
            public KeyValueString fromString(String string) {
                throw new UnsupportedOperationException("Converter not implemented");
            }
        });

    }

    private List<KeyValueStringLabel> loadLocation() {
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

    private List<KeyValueStringLabel> loadProfession() {
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

    // framework.search function for this combo
    private Function<String, List<KeyValueStringLabel>> searchFunctionParam(List<KeyValueStringLabel> items) {
        return term -> items.stream().filter(item -> item.getValue().contains(term == null ? "" : term)).collect(Collectors.toList());
    }

    // text fields formatter
    private Function<KeyValueStringLabel, String> textFieldFormatter = item -> String.format("%s", item.getValue());

    // label formatter
    private Function<KeyValueStringLabel, String> labelItemFormatter = item -> String.format("%s - %s", item.getKey(), item.getValue());

}