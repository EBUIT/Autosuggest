package sample.combobox;

import java.util.Random;

/**
 * Created by laurent on 11.02.2015.
 */
public class KeyValueStringImpl implements KeyValueString {
    private String value;
    private String key;
    private int random;

    public KeyValueStringImpl(String key, String value) {
        this.value = value;
        this.key = key;
        this.random = new Random().nextInt();
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public void setValue(String s) {
        this.value = s;
    }

    @Override
    public String getKey() {
        return key;
    }

    @Override
    public int getRandom() {
        return this.random;
    }

    @Override
    public void setRandom(int r) {
        this.random = r;
    }
}