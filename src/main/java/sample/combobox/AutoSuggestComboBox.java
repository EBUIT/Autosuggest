package sample.combobox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyEvent;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Created by sopra_om on 02.02.2015.
 *
 * @param <T> The type of the content managed
 */
public class AutoSuggestComboBox<T> extends ComboBox<T> {

    private static final KeyCodeCombination UP = new KeyCodeCombination(KeyCode.UP);
    private static final KeyCodeCombination DOWN = new KeyCodeCombination(KeyCode.DOWN);
    private static final KeyCodeCombination LEFT = new KeyCodeCombination(KeyCode.LEFT);
    private static final KeyCodeCombination RIGHT = new KeyCodeCombination(KeyCode.RIGHT);
    private static final KeyCodeCombination BACK_SPACE = new KeyCodeCombination(KeyCode.BACK_SPACE);
    private static final KeyCodeCombination DELETE = new KeyCodeCombination(KeyCode.DELETE);
    private static final KeyCodeCombination HOME = new KeyCodeCombination(KeyCode.HOME);
    private static final KeyCodeCombination TAB = new KeyCodeCombination(KeyCode.TAB);
    private static final KeyCodeCombination END = new KeyCodeCombination(KeyCode.END);
//    private static final KeyCodeCombination CTRL = new KeyCodeCombination(KeyCode.CONTROL);

    private Function<String, List<T>> searchFunction = (s) -> new ArrayList();

    public AutoSuggestComboBox() {
        this.setMaxWidth(Double.MAX_VALUE);
        this.getEditor().setFocusTraversable(true);
        this.setEditable(true);
    }

    /**
     * constructor
     * <p>
     * ex :
     * new AutoSuggestComboBox<>(term -> FXCollections.observableArrayList(framework.service.framework.search(term, 10)),
     * t -> t.getCode(),
     * t->  String.format("%s - %s", t.getCode(), t.getName()))
     *
     * @param id                  AutoSuggestBox identifier
     * @param searchFunctionParam searchFunction have a String in parameter corresponding to the term framework.search and return List<T>
     * @param textFieldFormatter  function to convert value (type of T) into String - Display in abc.sample.combobox textfield
     * @param labelItemFormatter  function to convert value (type of T) into String - Display in abc.sample.combobox item label
     */
    public AutoSuggestComboBox(String id, Function<String, List<T>> searchFunctionParam, Function<T, String> textFieldFormatter, Function<T, String> labelItemFormatter) {
        this();
        this.setId(id);
        init(searchFunctionParam, textFieldFormatter, labelItemFormatter);
    }

    /**
     * constructor
     * <p>
     * ex :
     * new AutoSuggestComboBox<>(term -> FXCollections.observableArrayList(framework.service.framework.search(term, 10)),
     * t -> t.getCode(),
     * t->  String.format("%s - %s", t.getCode(), t.getName()))
     *
     * @param searchFunctionParam searchFunction have a String in parameter corresponding to the term framework.search and return List<T>
     * @param textFieldFormatter  function to convert value (type of T) into String - Display in abc.sample.combobox textfield
     * @param labelItemFormatter  function to convert value (type of T) into String - Display in abc.sample.combobox item label
     * @deprecated use constructor with id
     */
    public AutoSuggestComboBox(Function<String, List<T>> searchFunctionParam, Function<T, String> textFieldFormatter, Function<T, String> labelItemFormatter) {
        this();

        init(searchFunctionParam, textFieldFormatter, labelItemFormatter);
    }

    /**
     * ex :
     * new AutoSuggestComboBox<>(term -> FXCollections.observableArrayList(framework.service.framework.search(term, 10)),
     * t -> t.getCode(),
     * t->  String.format("%s - %s", t.getCode(), t.getName()))
     *
     * @param searchFunctionParam searchFunction have a String in parameter corresponding to the term framework.search and return List<T>
     * @param textFieldFormatter  function to convert value (type of T) into String - Display in abc.sample.combobox textfield
     * @param labelItemFormatter  function to convert value (type of T) into String - Display in abc.sample.combobox item label
     */
    public void init(Function<String, List<T>> searchFunctionParam, Function<T, String> textFieldFormatter, Function<T, String> labelItemFormatter) {
        setSearchFunction(searchFunctionParam);
        setTextFieldFormatter(textFieldFormatter);
        setLabelItemFormatter(labelItemFormatter);

        // load on start
        List<T> result = searchFunction.apply(null);
        displayList(result);
    }


    protected void displayList(List<T> result) {
        if (!(result instanceof ObservableList)) {
            result = FXCollections.observableArrayList(result);
        }
        this.setItems((ObservableList<T>) result);
        this.addEventHandler(KeyEvent.KEY_PRESSED, t -> this.hide());
        this.addEventHandler(KeyEvent.KEY_RELEASED, createKeyReleaseEventHandler());
    }

    public void setLabelItemFormatter(Function<T, String> labelItemFormatter) {
        this.setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
            @Override
            public ListCell<T> call(ListView<T> param) {
                return new ListCell<T>() {
                    @Override
                    public void updateItem(T item,
                                           boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            setText(null);
                            return;
                        }

                        if (item != null) {
                            setText(item.toString());
                            if (labelItemFormatter != null) {
                                final String title = labelItemFormatter.apply(item);
                                setText(title);
                            }
                            super.setPrefWidth(this.getPrefWidth());

                        } else {
                            setText(null);
                        }
                    }
                };
            }
        });
    }


    public void setTextFieldFormatter(Function<T, String> textFieldFormatter) {
        super.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T t) {
                return t == null ? null : textFieldFormatter.apply(t);
            }

            @Override
            public T fromString(String string) {
                return AutoSuggestComboBox.this.getValue();
            }
        });
    }

    protected EventHandler<KeyEvent> createKeyReleaseEventHandler() {
        return new EventHandler<KeyEvent>() {

            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                String term = AutoSuggestComboBox.this.getEditor().getText();
                int termLength = 0;

                if (term != null) {
                    termLength = term.length();
                }

                if (UP.match(event)) {
                    caretPos = -1;
                    moveCaret(termLength);
                    return;
                } else if (DOWN.match(event)) {
                    if (!AutoSuggestComboBox.this.isShowing()) {
                        AutoSuggestComboBox.this.show();
                    }
                    caretPos = -1;
                    moveCaret(termLength);
                    return;
                } else if (BACK_SPACE.match(event)) {
                    moveCaretToPos = true;
                    caretPos = AutoSuggestComboBox.this.getEditor().getCaretPosition();
                } else if (DELETE.match(event)) {
                    moveCaretToPos = true;
                    caretPos = AutoSuggestComboBox.this.getEditor().getCaretPosition();
                }

                if (RIGHT.match(event) || LEFT.match(event) || event.isControlDown() || HOME.match(event) || END.match(event) || TAB.match(event)) {
                    return;
                }

                ObservableList<T> list = FXCollections.observableArrayList();

                List<T> listResult = searchFunction.apply(term);

                list.addAll(listResult);

                AutoSuggestComboBox.this.getSelectionModel().clearSelection();
                AutoSuggestComboBox.this.setItems(null);
                AutoSuggestComboBox.this.setItems(list);
                AutoSuggestComboBox.this.getEditor().setText(term);
                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(termLength);
                if (!list.isEmpty()) {
                    AutoSuggestComboBox.this.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    AutoSuggestComboBox.this.getEditor().positionCaret(textLength);
                } else {
                    AutoSuggestComboBox.this.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        };
    }

    protected Function<String, List<T>> getSearchFunction() {
        return searchFunction;
    }

    public void setSearchFunction(Function<String, List<T>> searchFunction) {
        this.searchFunction = searchFunction;
    }
}
