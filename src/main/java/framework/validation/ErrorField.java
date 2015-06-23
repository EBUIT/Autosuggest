package framework.validation;

/**
 * Created by sopra_om on 17.01.2015.
 */
public class ErrorField {
    /**
     * Error code
     */
    private String code;
    /**
     * Error description
     */
    private String description;

    private String level;

    private String detail;

    public ErrorField() {

    }

    public ErrorField(String code, String description) {

        this.code = code;
        this.description = description;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
