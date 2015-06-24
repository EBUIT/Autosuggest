package sample;

import framework.bean.search.Like;
import framework.bean.search.SearchCriteria;
import framework.service.SearchService;
import framework.service.SearchServiceFactory;
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
import sample.mockserver.MockDatas;

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
    private SearchServiceFactory searchServiceFactory;

    private ObjectProperty<KeyValueStringLabel> dataLocationProperty = new SimpleObjectProperty<>();
    private ObjectProperty<KeyValueStringLabel> dataprofessionProperty = new SimpleObjectProperty<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // WARN this is a workaround
        // the controller instance creation done by FXMLLoader avoids to Autowired properties/methods bean
        Main.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);

        // datas
        final List<KeyValueStringLabel> itemsLocation = MockDatas.loadLocation();
        final List<KeyValueStringLabel> itemsProfession = MockDatas.loadProfession();

        // init sample.autosuggest
        autosuggestLocation.init(searchFunctionParam(itemsLocation), textFieldFormatter, labelItemFormatter);
        autosuggestProfession.init(searchFunctionParam(itemsProfession), textFieldFormatter, labelItemFormatter);
        updateGenericAutoSuggest(autosuggestSearch, searchServiceFactory.searchService(ProfessionBean.class),
                t -> String.format("%s - %s", t.getCode().toString(), t.getName()), t -> String.format("%s - %s", t.getCode().toString(), t.getName()),
                "code", "name"
        );

        // bind with Labels
        bind();
    }

    public static void updateGenericAutoSuggest(
            AutoSuggestSearchRestClientMock<ProfessionBean> autoSuggest,
            SearchService<ProfessionBean> searchService,
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

    // framework.search function for this combo
    private Function<String, List<KeyValueStringLabel>> searchFunctionParam(List<KeyValueStringLabel> items) {
        return term -> items.stream().filter(item -> item.getValue().contains(term == null ? "" : term)).collect(Collectors.toList());
    }

    // text fields formatter
    private Function<KeyValueStringLabel, String> textFieldFormatter = item -> String.format("%s", item.getValue());

    // label formatter
    private Function<KeyValueStringLabel, String> labelItemFormatter = item -> String.format("%s - %s", item.getKey(), item.getValue());

}