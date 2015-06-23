package framework.validation;

import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;

import java.util.ArrayList;
import java.util.List;

/**
 * Field property
 * <p/>
 * Created by sopra_om on 16.01.2015.
 */
public class FieldProperty {
    /**
     * Error field which contain the message and the pathId
     */
    private final ListProperty<ErrorField> messages = new SimpleListProperty<>(FXCollections.observableList(new ArrayList<>()));
    private String pathId;

    /**
     * Default constructor
     */
    public FieldProperty() {
    }

    /**
     * Field pathId
     *
     * @param pathId
     */
    public FieldProperty(String pathId) {
        this();
        this.pathId = pathId;
    }

    public String getPathId() {
        return pathId;
    }

    public void setPathId(String pathId) {
        this.pathId = pathId;
    }

    /**
     * Return error field property
     *
     * @return error field property
     */
    public ListProperty<ErrorField> messagesProperty() {
        return messages;
    }

    public void clearError() {
        this.messages.clear();
    }

    public List<ErrorField> getMessages() {
        return messages.get();
    }

    public void setMessages(List<ErrorField> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
    }
}
