package sample;

import framework.bean.search.SearchCriteria;
import framework.service.SearchService;
import framework.service.SearchServiceFactory;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.autosuggest.AutoSuggestSearchRestClientMock;
import sample.combobox.*;
import sample.mockserver.MockDatas;

import javax.annotation.Resource;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Controller implements Initializable {
    private final static Logger LOG = LoggerFactory.getLogger(Controller.class);

    @FXML
    AutoSuggestKeyValueString autosuggestProfession = new AutoSuggestKeyValueString();
    @FXML
    AutoSuggestSearchRestClientMock<LocationBean> autosuggestSearch; // org.fxpart.AutoSuggestFX autoSuggestFX;
    @FXML
    ComboBox comboFx1, comboFx2;
    @FXML
    PartTextDecoComboBox partTextDecoLocation;


    @Resource
    private SearchServiceFactory searchServiceFactory;

    private ObjectProperty<KeyValueStringLabel> dataLocationProperty = new SimpleObjectProperty<>();
    private ObjectProperty<KeyValueStringLabel> dataprofessionProperty = new SimpleObjectProperty<>();
    private ObjectProperty<KeyValueStringLabel> partDecoDataLocationProperty = new SimpleObjectProperty<>();

    private final ObservableList strings = FXCollections.observableArrayList(MockDatas.loadLocationStrings());

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // WARN this is a workaround
        // the controller instance creation done by FXMLLoader avoids to Autowired properties/methods bean
        Main.applicationContext.getAutowireCapableBeanFactory().autowireBean(this);

        // datas
        final List<KeyValueStringLabel> itemsLocation = MockDatas.loadLocation();

        // old combos. KeyValue and SearchRest
        autosuggestProfession.init(searchFunctionParam(itemsLocation), textFieldFormatter, labelItemFormatter);
        updateGenericAutoSuggest(autosuggestSearch, searchServiceFactory.searchService(LocationBean.class), t -> String.format("%s - %s", t.getCode().toString(), t.getName()), t -> String.format("%s - %s", t.getCode().toString(), t.getName()), "code", "name");

        // FxUtils combos
        comboFx1.setItems(strings);
        comboFx2.setItems(strings);
        FxUtils.autoCompleteComboBox(comboFx1, FxUtils.AutoCompleteMode.CONTAINING);
        FxUtils2.autoCompleteComboBox(comboFx2, FxUtils2.AutoCompleteMode.STARTS_WITH);

        //pavel
        partTextDecoLocation.init(searchFunctionParam(itemsLocation), textFieldFormatter);

    }

    public static void updateGenericAutoSuggest(
            AutoSuggestSearchRestClientMock<LocationBean> autoSuggest,
            SearchService<LocationBean> searchService,
            Function<LocationBean, String> txtField,
            Function<LocationBean, String> cellField,
            String code,
            String name) {
        autoSuggest.init(searchService, txtField, cellField,
                s -> SearchCriteria.of().likeBegin(code, s)
                //,s1 -> SearchCriteria.of().and(notlike(code, s1, Like.LikeType.BEGIN), or(like(code, s1), like(name, s1)))
        );
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