package com.nf.vi.needfoodshipper.Constructor;

/**
 * Created by Minh Nhat on 7/12/2017.
 */

public class ListLangContructor {
    public String id;
    public String lang;

    public ListLangContructor(String id, String lang) {
        this.id = id;
        this.lang = lang;
    }

    public ListLangContructor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }
}
