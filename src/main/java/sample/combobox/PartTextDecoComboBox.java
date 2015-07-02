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
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sample.mockserver.MockDatas;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Created by Pavlo on 30.06.2015.
 */
public class PartTextDecoComboBox<T> extends ComboBox<T> {

    private static final Logger LOG = LoggerFactory.getLogger(PartTextDecoComboBox.class);

    private static final KeyCodeCombination UP = new KeyCodeCombination(KeyCode.UP);
    private static final KeyCodeCombination DOWN = new KeyCodeCombination(KeyCode.DOWN);
    private static final KeyCodeCombination LEFT = new KeyCodeCombination(KeyCode.LEFT);
    private static final KeyCodeCombination RIGHT = new KeyCodeCombination(KeyCode.RIGHT);
    private static final KeyCodeCombination BACK_SPACE = new KeyCodeCombination(KeyCode.BACK_SPACE);
    private static final KeyCodeCombination DELETE = new KeyCodeCombination(KeyCode.DELETE);
    private static final KeyCodeCombination HOME = new KeyCodeCombination(KeyCode.HOME);
    private static final KeyCodeCombination TAB = new KeyCodeCombination(KeyCode.TAB);
    private static final KeyCodeCombination END = new KeyCodeCombination(KeyCode.END);

    private static final String HIGHLIGHTED_CLASS = "highlighted-dropdown";
    private static final String USUAL_CLASS = "usual-dropdown";

    private Function<String, List<KeyValueString>> searchFunction;

    public PartTextDecoComboBox() {
        setEditable(true);
    }

    public List<KeyValueString> getDatas() {
        // TODO call a datasource
        return new MockDatas().loadLocation();
    }

    public void init(Function<T, String> textFieldFormatter) {
        init();
        this.searchFunction = term -> getDatas().stream().filter(item -> item.getValue().contains(term == null ? "" : term)).collect(Collectors.toList());
        setTextFieldFormatter(textFieldFormatter);
    }

    public void init() {
        setVisibleRowCount(10);
        setCustomCellFactory();
        addEventHandler(KeyEvent.KEY_PRESSED, t -> this.hide());
        addEventHandler(KeyEvent.KEY_RELEASED, createKeyReleaseEventHandler());
    }

    protected EventHandler<KeyEvent> createKeyReleaseEventHandler() {
        return new EventHandler<KeyEvent>() {
            private boolean moveCaretToPos = false;
            private int caretPos;

            @Override
            public void handle(KeyEvent event) {
                String term = PartTextDecoComboBox.this.getEditor().getText();
                int termLength = 0;

                if (term != null) {
                    termLength = term.length();
                }

                if (UP.match(event)) {
                    caretPos = -1;
                    moveCaret(termLength);
                    return;
                } else if (DOWN.match(event)) {
                    if (!PartTextDecoComboBox.this.isShowing()) {
                        PartTextDecoComboBox.this.show();
                    }
                    caretPos = -1;
                    moveCaret(termLength);
                    return;
                } else if (BACK_SPACE.match(event)) {
                    moveCaretToPos = true;
                    caretPos = PartTextDecoComboBox.this.getEditor().getCaretPosition();
                } else if (DELETE.match(event)) {
                    moveCaretToPos = true;
                    caretPos = PartTextDecoComboBox.this.getEditor().getCaretPosition();
                }

                if (RIGHT.match(event) || LEFT.match(event) || event.isControlDown() || HOME.match(event) || END.match(event) || TAB.match(event)) {
                    return;
                }

                // datas filtering
//                ObservableList<T> list = FXCollections.observableArrayList((Collection<? extends T>) search().apply(term));
                ObservableList<T> list = FXCollections.observableArrayList((Collection<? extends T>) searchFunction.apply(term));
                PartTextDecoComboBox.this.setItems(list);

                if (!moveCaretToPos) {
                    caretPos = -1;
                }
                moveCaret(termLength);
                if (!list.isEmpty()) {
                    PartTextDecoComboBox.this.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    PartTextDecoComboBox.this.getEditor().positionCaret(textLength);
                } else {
                    PartTextDecoComboBox.this.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        };
    }

    private void setCustomCellFactory() {
        setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
                           @Override
                           public ListCell<T> call(ListView<T> param) {

                               final ListCell<T> cell = new ListCell<T>() {
                                   @Override
                                   protected void updateItem(T item, boolean empty) {
                                       super.updateItem(item, empty);
                                       if (item == null || empty) {
                                           setText(null);
                                           setGraphic(null);
                                       } else {
                                           String keyString = (String) ((KeyValueStringLabel) item).getKey();
                                           String valueString = ((KeyValueStringLabel) item).getValue();
                                           String itemString = keyString + " - " + valueString;
                                           String searchString = PartTextDecoComboBox.this.getEditor().getText();
                                           if (searchString.length() != 0) {
                                               Integer searchStringPosition = valueString.indexOf(searchString);

                                               // itemString contains searchString. It should be split and searchString should be highLighted
                                               if (searchStringPosition >= 0) {
                                                   String beginString = valueString.substring(0, searchStringPosition);
                                                   String highlightedString = valueString.substring(searchStringPosition, searchStringPosition + searchString.length());
                                                   String endString = valueString.substring(searchStringPosition + searchString.length());

                                                   TextFlow cellTextFlow = new TextFlow();

                                                   Text separator = new Text(keyString + " - ");
                                                   separator.getStyleClass().add(USUAL_CLASS);
                                                   cellTextFlow.getChildren().add(separator);

                                                   // If I remove this check - the wrong symbol is highlighted. Looks like bug in Text or TextFlow.
                                                   if (beginString.length() > 0) {
                                                       final Text begin = new Text(beginString);
                                                       begin.getStyleClass().add(USUAL_CLASS);
                                                       cellTextFlow.getChildren().add(begin);
                                                   }

                                                   final Text highlighted = new Text(highlightedString);
                                                   highlighted.getStyleClass().add(HIGHLIGHTED_CLASS);
                                                   cellTextFlow.getChildren().add(highlighted);

                                                   final Text end = new Text(endString);
                                                   end.getStyleClass().add(USUAL_CLASS);
                                                   cellTextFlow.getChildren().add(end);

                                                   setGraphic(cellTextFlow);
                                               } else {
                                                   setText(itemString);
                                               }
                                           } else {
                                               setText(itemString);
                                           }
                                       }
                                   }
                               };
                               return cell;
                           }
                       }
        );
    }

    private void setTextFieldFormatter(Function<T, String> textFieldFormatter) {
        super.setConverter(new StringConverter<T>() {
            @Override
            public String toString(T t) {
                return t == null ? null : textFieldFormatter.apply(t);
            }

            @Override
            public T fromString(String string) {
                return PartTextDecoComboBox.this.getValue();
            }
        });
    }
}