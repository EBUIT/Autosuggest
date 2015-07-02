package sample.combobox;

/**
 * Created by laurent on 11.02.2015.
 */
public interface KeyValueString extends KeyValueStringLabel<String> {

    String getValue();
    void setValue(String s);

    String getKey();

    int getRandom();
    void setRandom(int r);
}
