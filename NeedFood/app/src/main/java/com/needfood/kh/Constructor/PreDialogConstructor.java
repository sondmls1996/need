package com.needfood.kh.Constructor;

/**
 * Created by Vi on 9/7/2017.
 */

public class PreDialogConstructor {
    public String quanli;
    public String price;
    public String title;
    public String id;
    public String typemn;

    public PreDialogConstructor(String quanli, String price, String title, String id, String typemn) {
        this.quanli = quanli;
        this.price = price;
        this.title = title;
        this.id = id;
        this.typemn = typemn;
    }

    public String getQuanli() {
        return quanli;
    }

    public void setQuanli(String quanli) {
        this.quanli = quanli;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTypemn() {
        return typemn;
    }

    public void setTypemn(String typemn) {
        this.typemn = typemn;
    }
}
