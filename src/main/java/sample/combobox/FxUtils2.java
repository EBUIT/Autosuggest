package sample.combobox;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.ComboBox;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * Created by metairie on 01-Jul-15.
 */
public class FxUtils2 {
    static String some;
    static String typedText;
    static StringBuilder sb = new StringBuilder();

    public enum AutoCompleteMode {
        STARTS_WITH, CONTAINING,;
    }


    public static <T> void autoCompleteComboBox(ComboBox<T> comboBox, AutoCompleteMode mode) {
        comboBox.setEditable(true);

        ObservableList<T> data = comboBox.getItems();

        comboBox.addEventHandler(KeyEvent.KEY_PRESSED, new EventHandler<KeyEvent>() {
            public void handle(KeyEvent event) {
                comboBox.hide();
            }
        });
        comboBox.addEventHandler(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            private boolean moveCaretToPos = false;
            private int caretPos;

            public void handle(KeyEvent event) {

                String keyPressed = event.getCode().toString().toLowerCase();


                if ("space".equals(keyPressed)) {
                    typedText = " ";
                } else if ("shift".equals(keyPressed) || "command".equals(keyPressed)
                        || "alt".equals(keyPressed)) {

                    return;
                } else {
                    typedText = event.getCode().toString().toLowerCase();
                }


                if (event.getCode() == KeyCode.UP) {
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.DOWN) {
                    if (!comboBox.isShowing()) {
                        comboBox.show();
                    }
                    caretPos = -1;
                    moveCaret(comboBox.getEditor().getText().length());
                    return;
                } else if (event.getCode() == KeyCode.BACK_SPACE) {
                    moveCaretToPos = true;

                    caretPos = comboBox.getEditor().getCaretPosition();
                    typedText = null;
                    sb.delete(0, sb.length());
                    comboBox.getEditor().setText(null);
                    return;

                } else if (event.getCode() == KeyCode.DELETE) {
                    moveCaretToPos = true;
                    caretPos = comboBox.getEditor().getCaretPosition();
                    return;
                } else if (event.getCode().equals(KeyCode.TAB)) {

                    some = null;
                    typedText = null;
                    sb.delete(0, sb.length());
                    return;
                } else if (event.getCode() == KeyCode.LEFT
                        || event.isControlDown() || event.getCode() == KeyCode.HOME
                        || event.getCode() == KeyCode.END || event.getCode() == KeyCode.RIGHT) {

                    return;
                }


                if (typedText == null) {
                    typedText = comboBox.getEditor().getText().toLowerCase();
                    sb.append(typedText);

                } else {
                    System.out.println("sb:" + sb);
                    System.out.println("tt:" + typedText);

                    sb.append(typedText);


                }

                ObservableList<T> list = FXCollections.observableArrayList();
                for (T aData : data) {

                    if (mode.equals(AutoCompleteMode.STARTS_WITH) && aData.toString().toLowerCase().startsWith(sb.toString())) {
                        list.add(aData);
                        some = aData.toString();
                    } else if (mode.equals(AutoCompleteMode.CONTAINING) && aData.toString().toLowerCase().contains(comboBox.getEditor().getText().toLowerCase())) {
                        list.add(aData);
                    }
                }


                comboBox.setItems(list);

                comboBox.getEditor().setText(some);


                comboBox.getEditor().positionCaret(sb.length());
                comboBox.getEditor().selectEnd();
                if (!moveCaretToPos) {
                    caretPos = -1;
                }

                if (!list.isEmpty()) {
                    comboBox.show();
                }
            }

            private void moveCaret(int textLength) {
                if (caretPos == -1) {
                    comboBox.getEditor().positionCaret(textLength);
                } else {
                    comboBox.getEditor().positionCaret(caretPos);
                }
                moveCaretToPos = false;
            }
        });
    }

    public static <T> T getComboBoxValue(ComboBox<T> comboBox) {
        if (comboBox.getSelectionModel().getSelectedIndex() < 0) {
            return null;
        } else {
            return comboBox.getItems().get(comboBox.getSelectionModel().getSelectedIndex());
        }
    }
}