package com.needfood.kh.Constructor;

/**
 * Created by admin on 21/07/2017.
 */

public class TranfConstructor {
    String id;
    String mess;
    String time;
    String coin;
    String idu;
    String note;

    public TranfConstructor() {
    }

    public TranfConstructor(String id, String mess, String time, String coin, String idu, String note) {
        this.id = id;
        this.mess = mess;
        this.time = time;
        this.coin = coin;
        this.idu = idu;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMess() {
        return mess;
    }

    public void setMess(String mess) {
        this.mess = mess;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getIdu() {
        return idu;
    }

    public void setIdu(String idu) {
        this.idu = idu;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
