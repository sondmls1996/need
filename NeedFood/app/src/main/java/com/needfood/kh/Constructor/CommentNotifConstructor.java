package com.needfood.kh.Constructor;

/**
 * Created by Vi on 5/5/2017.
 */

public class CommentNotifConstructor {
    public String imgcm;
    public String name;
    public String content;

    public CommentNotifConstructor(String imgcm, String name, String content) {
        this.imgcm = imgcm;
        this.name = name;
        this.content = content;
    }

    public String getImgcm() {
        return imgcm;
    }

    public void setImgcm(String imgcm) {
        this.imgcm = imgcm;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
