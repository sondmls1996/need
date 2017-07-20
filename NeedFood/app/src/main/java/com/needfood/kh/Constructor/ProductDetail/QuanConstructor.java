package com.needfood.kh.Constructor.ProductDetail;

/**
 * Created by Vi on 4/27/2017.
 */

public class QuanConstructor {
    public String id;
    public  String img;
    public String name;

    public QuanConstructor(String id, String img, String name) {
        this.id = id;
        this.img = img;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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
}
