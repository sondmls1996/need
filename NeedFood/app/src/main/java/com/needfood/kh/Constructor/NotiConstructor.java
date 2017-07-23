package com.needfood.kh.Constructor;

/**
 * Created by admin on 23/07/2017.
 */

public class NotiConstructor {
    String title;
    String img;
    String time;

    public NotiConstructor() {
    }

    public NotiConstructor(String title, String img, String time) {
        this.title = title;
        this.img = img;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
