package com.needfood.kh.Constructor;

/**
 * Created by Vi on 4/25/2017.
 */

public class NotifConstructor {
    public String linkimg;
    public String content;
    public String time;

    public NotifConstructor(String linkimg, String content, String time) {
        this.linkimg = linkimg;
        this.content = content;
        this.time = time;
    }

    public String getLinkimg() {
        return linkimg;
    }

    public void setLinkimg(String linkimg) {
        this.linkimg = linkimg;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
