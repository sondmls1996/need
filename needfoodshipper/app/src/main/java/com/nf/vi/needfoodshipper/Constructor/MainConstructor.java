package com.nf.vi.needfoodshipper.Constructor;

/**
 * Created by Minh Nhat on 4/28/2017.
 */

public class MainConstructor {
    public String id;
    public String order;
    public String lc;
    public String ct;
    public String re;
    public String tl;
    public String pay;
    public String moneyship;
    public String stt;
    public String code;
    public String soluong;
    public String sanpham;
    public String dongia;
    public String thanhtien;

    public MainConstructor() {
    }

    public MainConstructor(String id, String order, String lc, String ct, String re, String tl, String pay, String moneyship, String stt, String code, String soluong, String sanpham, String dongia, String thanhtien) {
        this.id = id;
        this.order = order;
        this.lc = lc;
        this.ct = ct;
        this.re = re;
        this.tl = tl;
        this.pay = pay;
        this.moneyship = moneyship;
        this.stt = stt;
        this.code = code;
        this.soluong = soluong;
        this.sanpham = sanpham;
        this.dongia = dongia;
        this.thanhtien = thanhtien;
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

    public String getMoneyship() {
        return moneyship;
    }

    public void setMoneyship(String moneyship) {
        this.moneyship = moneyship;
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

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getSanpham() {
        return sanpham;
    }

    public void setSanpham(String sanpham) {
        this.sanpham = sanpham;
    }

    public String getDongia() {
        return dongia;
    }

    public void setDongia(String dongia) {
        this.dongia = dongia;
    }

    public String getThanhtien() {
        return thanhtien;
    }

    public void setThanhtien(String thanhtien) {
        this.thanhtien = thanhtien;
    }
}