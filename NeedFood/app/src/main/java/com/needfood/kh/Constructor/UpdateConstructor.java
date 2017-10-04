package com.needfood.kh.Constructor;

/**
 * Created by Vi on 10/4/2017.
 */

public class UpdateConstructor {

    public String row;
    public String value;

    public UpdateConstructor(String row, String value) {
        this.row = row;
        this.value = value;
    }

    public String getRow() {
        return row;
    }

    public void setRow(String row) {
        this.row = row;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
