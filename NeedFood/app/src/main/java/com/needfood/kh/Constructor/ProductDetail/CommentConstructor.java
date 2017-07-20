package com.needfood.kh.Constructor.ProductDetail;

/**
 * Created by Vi on 4/27/2017.
 */

public class CommentConstructor {
    public String link;
    public String name;
    public String content;
    public long time;

    public CommentConstructor(String link, String name, String content, long time) {
        this.link = link;
        this.name = name;
        this.content = content;
        this.time = time;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
