package framework.validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sopra_om on 16.01.2015.
 */
public class ValidationErrorBean {
    private List<FieldProperty> errors = new ArrayList<>();

    private Object data;

    public ValidationErrorBean() {

    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public List<FieldProperty> getErrors() {
        return errors;
    }

    public void setErrors(List<FieldProperty> errors) {
        this.errors = errors;
    }
}
