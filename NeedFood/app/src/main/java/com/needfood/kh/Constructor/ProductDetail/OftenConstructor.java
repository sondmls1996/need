package com.needfood.kh.Constructor.ProductDetail;

/**
 * Created by Vi on 4/27/2017.
 */

public class OftenConstructor {
    public String img;
    public String name;
    public String prize;
    public String mn;
    public String dv;
    public boolean isSelected = false;
    public String bar;
    public String code;
    public String note;
    public String id;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getBar() {
        return bar;
    }

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public OftenConstructor(String img, String name, String prize, String mn, String dv, boolean isSelected, String bar, String code, String note, String id) {
        this.img = img;
        this.name = name;
        this.prize = prize;
        this.mn = mn;
        this.dv = dv;
        this.isSelected = isSelected;
        this.bar = bar;
        this.code = code;
        this.note = note;
        this.id = id;
    }
}
