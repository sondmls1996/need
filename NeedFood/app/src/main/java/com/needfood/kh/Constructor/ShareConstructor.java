package com.needfood.kh.Constructor;

/**
 * Created by Vi on 9/12/2017.
 */

public class ShareConstructor {
    public String ids;
    public String days;
    public String sttshare;

    public ShareConstructor(String ids, String days, String sttshare) {
        this.ids = ids;
        this.days = days;
        this.sttshare = sttshare;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public String getSttshare() {
        return sttshare;
    }

    public void setSttshare(String sttshare) {
        this.sttshare = sttshare;
    }
}
