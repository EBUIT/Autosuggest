package sample.combobox;

import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Auto suggest for static list with  KeyValue
 */

public class AutoSuggestKeyValueString extends AutoSuggestComboBox<KeyValueStringLabel> {


    private static final Function<KeyValueStringLabel, String> textFieldFormatter = (KeyValueStringLabel keyValue) -> {
        return keyValue.getValue();
    };
    private static final Function<KeyValueStringLabel, String> labelItemFormatter = (KeyValueStringLabel keyValue) -> {
        return keyValue.getValue();
    };

    public AutoSuggestKeyValueString() {

    }

    public AutoSuggestKeyValueString(Collection<? extends KeyValueStringLabel> list) {
        init(list);
    }

    private static Function<String, List<KeyValueStringLabel>> createSearchFunction(Collection<? extends KeyValueStringLabel> list) {
        return new Function<String, List<KeyValueStringLabel>>() {
            @Override
            public List<KeyValueStringLabel> apply(String term) {
                Stream<? extends KeyValueStringLabel> stream = list.stream().filter((KeyValueStringLabel keyValue) -> keyValue.getValue().toUpperCase().contains(term == null ? "" : term.toUpperCase()));
                List<KeyValueStringLabel> result = stream.collect(Collectors.toCollection(ArrayList<KeyValueStringLabel>::new));
                return FXCollections.observableArrayList(result);
            }
        };
    }

    public void init(Collection<? extends KeyValueStringLabel> list) {
        super.init(createSearchFunction(list), textFieldFormatter, labelItemFormatter);
    }

    public void setChoiceList(Collection<? extends KeyValueStringLabel> list) {
        final Function<String, List<KeyValueStringLabel>> searchFunction = createSearchFunction(list);
        setSearchFunction(searchFunction);
    }




}
