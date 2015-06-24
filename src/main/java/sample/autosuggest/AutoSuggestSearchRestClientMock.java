package sample.autosuggest;

import framework.bean.search.SearchCriteria;
import framework.rest.RestClientParameters;
import framework.rest.task.SearchRestClient;
import framework.service.SearchService;
import javafx.collections.FXCollections;
import javafx.concurrent.Service;
import javafx.concurrent.WorkerStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.combobox.AutoSuggestComboBox;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

import static com.google.common.collect.Lists.asList;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Auto suggest with Search Criteria
 * <p>
 */
public class AutoSuggestSearchRestClientMock<T> extends AutoSuggestComboBox<T> {

    public static final int DEFAULT_OCCURENCE = 30;
    private static final Logger LOG = LoggerFactory.getLogger(AutoSuggestSearchRestClientMock.class);
    private boolean loaded = false;

    /**
     * Init AutoSuggestSearchRestClient for 10 occurences with:
     *
     * @param searchService
     * @param textFieldFormatter - Function for text Field Formatter
     * @param labelItemFormatter - Function for Label formatter
     * @param searchCriterias    -
     */
    public void init(final SearchService<T> searchService, final Function<T, String> textFieldFormatter, final Function<T, String> labelItemFormatter, final Function<String, SearchCriteria> firstSearchCriteria, final Function<String, SearchCriteria>... searchCriterias) {
        init(searchService, textFieldFormatter, labelItemFormatter, DEFAULT_OCCURENCE, firstSearchCriteria, searchCriterias);
    }

    /**
     * Init AutoSuggestSearchRestClient for {@code occurences} occurences with:
     *
     * @param searchService
     * @param textFieldFormatter - Function for text Field Formatter
     * @param labelItemFormatter - Function for Label formatter
     * @param occurences         - number of occurence to display
     * @param searchCriterias    -
     * @Deprecated - Use init with SearchService
     */
    public void init(final SearchService<T> searchService, final Function<T, String> textFieldFormatter, final Function<T, String> labelItemFormatter, final int occurences, final Function<String, SearchCriteria> firstSearchCriteria, final Function<String, SearchCriteria>... searchCriterias) {
        super.init(term ->
                {
                    List<T> result = new ArrayList<>();
                    searchElements(searchService, textFieldFormatter, occurences, firstSearchCriteria, term, result, searchCriterias);
                    loaded = true;
                    return FXCollections.observableArrayList(result);
                },
                textFieldFormatter,
                labelItemFormatter);

    }

    private void searchElements(SearchService<T> searchService, Function<T, String> textFieldFormatter, int occurences, Function<String, SearchCriteria> firstSearchCriteria, String term, List<T> result, Function<String, SearchCriteria>[] searchCriterias) {
        //HACK Use to manage the sample.autosuggest lock mode
        if (null != term) {
            result.addAll(searchLocked(textFieldFormatter, searchService, occurences, firstSearchCriteria, term, searchCriterias));
        } else {
            searchWithService(searchService, occurences, firstSearchCriteria, term);
        }
    }

    private void searchWithService(SearchService<T> searchService, int occurences, Function<String, SearchCriteria> firstSearchCriteria, String term) {
        List<T> result = newArrayList();
        Service<List<T>> service = search(searchService, term, occurences, firstSearchCriteria);
        service.setOnSucceeded((WorkerStateEvent event) -> {
            result.addAll((List<T>) event.getSource().getValue());
            this.setItems(FXCollections.observableArrayList(result));

        });
    }

    // TODO bad implementation to simplify
    // we add first search result (firstSearchCriteria) with a second (!) search request (searchCriterias)
    private Collection<T> searchLocked(Function<T, String> textFieldFormatter, SearchService<T> searchService, int occurences, Function<String, SearchCriteria> firstSearchCriteria, String term, Function<String, SearchCriteria>[] searchCriterias) {
        List<T> result = newArrayList();
        int elementToSearch = occurences;
        for (Function<String, SearchCriteria> function : asList(firstSearchCriteria, searchCriterias)) {
            result.addAll(search((SearchRestClient<T>) searchService.getPostListRestClient(), term, elementToSearch, function));
        }
        return result;
    }

    /**
     * Launch the framework.search
     */
    private List<T> search(SearchRestClient<T> clientRest, String term, int elementToSearch, Function<
            String, SearchCriteria> function) {
        SearchCriteria searchCriteria = SearchCriteria.of();
        if (term != null) {
            searchCriteria = function.apply(term);
            searchCriteria.setMax(elementToSearch);
        }
        RestClientParameters innerParameters = RestClientParameters.of(searchCriteria);
        return clientRest.apply(innerParameters);
    }

    private Service search(SearchService<T> searchService, String term, int elementToSearch, Function<String, SearchCriteria> function) {
        if (term == null) {
            term = "";
        }

        SearchCriteria searchCriteria = function.apply(term);
        searchCriteria.setMax(elementToSearch);

        Service<List<T>> service = searchService.search(searchCriteria);
        service.start();
        return service;
    }

    public boolean isLoaded() {
        return loaded;
    }


}
