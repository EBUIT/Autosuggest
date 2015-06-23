package sample.combobox;

public interface RequiredComponent {
    // Style for Required field
    String REQUIRED_CSS = "required";

    void addRequiredField();

    void removeRequiredField();

    boolean isRequired();

    void setRequired(boolean required);
}
