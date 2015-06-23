package sample.combobox;

/**
 * Created by laurent on 31.03.2015.
 */
public class KeyValueStringLabelImpl<K> implements KeyValueStringLabel<K> {

    private String value;
    private K key;

    public KeyValueStringLabelImpl(K key, String value) {
        this.value = value;
        this.key = key;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public K getKey() {
        return key;
    }


}
