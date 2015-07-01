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
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Callback;
import javafx.util.StringConverter;

import java.util.List;
import java.util.function.Function;

/**
 * Created by Pavlo on 30.06.2015.
 */
public class PartTextDecoComboBox<T> extends ComboBox<T> {

    private static final KeyCodeCombination UP = new KeyCodeCombination(KeyCode.UP);
    private static final KeyCodeCombination DOWN = new KeyCodeCombination(KeyCode.DOWN);
    private static final KeyCodeCombination LEFT = new KeyCodeCombination(KeyCode.LEFT);
    private static final KeyCodeCombination RIGHT = new KeyCodeCombination(KeyCode.RIGHT);
    private static final KeyCodeCombination BACK_SPACE = new KeyCodeCombination(KeyCode.BACK_SPACE);
    private static final KeyCodeCombination DELETE = new KeyCodeCombination(KeyCode.DELETE);
    private static final KeyCodeCombination HOME = new KeyCodeCombination(KeyCode.HOME);
    private static final KeyCodeCombination TAB = new KeyCodeCombination(KeyCode.TAB);
    private static final KeyCodeCombination END = new KeyCodeCombination(KeyCode.END);

    private Function<String, List<T>> searchFunction;
    /*
    This style applied to AutoSuggestComboBox dropdown list
     */
    private static enum DropDownStyle {HIGHLIGHTED, USUAL};
    /*
    CSS style class names
     */
    private static final String HIGHLIGHTED_CLASS = "highlighted-dropdown";
    private static final String USUAL_CLASS = "usual-dropdown";

    public PartTextDecoComboBox(){
        setEditable(true);
    }

    public void init(Function<String, List<T>> searchFunction,
                                Function<T, String> textFieldFormatter) {

        setVisibleRowCount(10);
        this.searchFunction = searchFunction;
        setTextFieldFormatter(textFieldFormatter);

        setCustomCellFactory();

        ObservableList<T> list = FXCollections.observableArrayList(searchFunction.apply(null));
        setItems((ObservableList<T>) list);

        addEventHandler(KeyEvent.KEY_PRESSED, t -> this.hide());
        addEventHandler(KeyEvent.KEY_RELEASED, createKeyReleaseEventHandler());
    }

    protected void displayList(List<T> result) {
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

                /*
                list should be instantiated on every key release
                 */
                ObservableList<T> list = FXCollections.observableArrayList(searchFunction.apply(term));

                /*
                On key released dropDown style should be applied
                 */
                //PartTextDecoComboBox.this.setCustomCellFactory();

                PartTextDecoComboBox.this.getSelectionModel().clearSelection();
                //PartTextDecoComboBox.this.setItems(null);
                PartTextDecoComboBox.this.setItems(list);
                PartTextDecoComboBox.this.getEditor().setText(term);
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

    private void setCustomCellFactory(){
        setCellFactory(new Callback<ListView<T>, ListCell<T>>() {
                @Override
                public ListCell<T> call(ListView<T> param) {
                    final ListCell<T> cell = new ListCell<T>(){
                        @Override
                        protected void updateItem(T item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText(null);
                                setGraphic(null);
                            }else{
                                String keyString=(String)((KeyValueStringLabel)item).getKey();
                                String valueString=((KeyValueStringLabel)item).getValue();
                                String itemString=keyString+" - "+valueString;

                                String searchString = PartTextDecoComboBox.this.getEditor().getText();
                                if (searchString.length()!=0){
                                    Integer searchStringPosition = valueString.indexOf(searchString);
                                    /*
                                    itemString contains searchString. It should be split and
                                    searchString should be highLighted
                                     */
                                    if (searchStringPosition>=0){
                                        String beginString = valueString.substring(0,searchStringPosition);
                                        String highlightedString = valueString.substring(searchStringPosition,
                                                searchStringPosition+searchString.length());
                                        String endString = valueString.substring(
                                                searchStringPosition+searchString.length());

                                        TextFlow cellTextFlow = new TextFlow();

                                        Text t = new Text(keyString+" - ");
                                        t.getStyleClass().add(USUAL_CLASS);
                                        cellTextFlow.getChildren().add(t);

                                        /*
                                        If I remove this check - the wrong symbol highlighted.
                                        Looks like bug in Text or TextFlow.
                                         */
                                        if (beginString.length()!=0){
                                            t = new Text(beginString);
                                            t.getStyleClass().add(USUAL_CLASS);
                                            cellTextFlow.getChildren().add(t);
                                        }

                                        t = new Text(highlightedString);
                                        t.getStyleClass().add(HIGHLIGHTED_CLASS);
                                        cellTextFlow.getChildren().add(t);

                                        t = new Text(endString);
                                        t.getStyleClass().add(USUAL_CLASS);
                                        cellTextFlow.getChildren().add(t);

                                        setGraphic(cellTextFlow);
                                    }else{
                                        setText(itemString);
                                    }
                                }else{
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