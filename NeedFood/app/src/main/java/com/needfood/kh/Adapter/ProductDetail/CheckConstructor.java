package com.needfood.kh.Adapter.ProductDetail;

/**
 * Created by Vi on 7/19/2017.
 */

public class CheckConstructor {

    public String quanli;
    public String price;
    public String tickkm;
    public String tickkm2;
    public String tickkm3;
    public String barcode;
    public String code;
    public String title;
    public String money;
    public String note;
    public String id;

    public CheckConstructor(String quanli, String price, String tickkm, String tickkm2, String tickkm3, String barcode, String code, String title, String money, String note, String id) {
        this.quanli = quanli;
        this.price = price;
        this.tickkm = tickkm;
        this.tickkm2 = tickkm2;
        this.tickkm3 = tickkm3;
        this.barcode = barcode;
        this.code = code;
        this.title = title;
        this.money = money;
        this.note = note;
        this.id = id;
    }

    public String getQuanli() {
        return quanli;
    }

    public void setQuanli(String quanli) {
        this.quanli = quanli;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTickkm() {
        return tickkm;
    }

    public void setTickkm(String tickkm) {
        this.tickkm = tickkm;
    }

    public String getTickkm2() {
        return tickkm2;
    }

    public void setTickkm2(String tickkm2) {
        this.tickkm2 = tickkm2;
    }

    public String getTickkm3() {
        return tickkm3;
    }

    public void setTickkm3(String tickkm3) {
        this.tickkm3 = tickkm3;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
