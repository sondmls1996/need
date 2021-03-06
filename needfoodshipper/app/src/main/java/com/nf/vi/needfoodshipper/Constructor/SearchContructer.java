package com.nf.vi.needfoodshipper.Constructor;

/**
 * Created by Minh Nhat on 7/16/2017.
 */

public class SearchContructer {
    public String id;
    public String order;
    public String lc;
    public String ct;
    public String re;
    public String tl;
    public String pay;
    public String stt;
    public String code;

    public SearchContructer(String id, String order, String lc, String ct, String re, String tl, String pay, String stt, String code) {
        this.id = id;
        this.order = order;
        this.lc = lc;
        this.ct = ct;
        this.re = re;
        this.tl = tl;
        this.pay = pay;
        this.stt = stt;
        this.code = code;
    }

    public SearchContructer() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getLc() {
        return lc;
    }

    public void setLc(String lc) {
        this.lc = lc;
    }

    public String getCt() {
        return ct;
    }

    public void setCt(String ct) {
        this.ct = ct;
    }

    public String getRe() {
        return re;
    }

    public void setRe(String re) {
        this.re = re;
    }

    public String getTl() {
        return tl;
    }

    public void setTl(String tl) {
        this.tl = tl;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    public String getStt() {
        return stt;
    }

    public void setStt(String stt) {
        this.stt = stt;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
