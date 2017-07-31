package com.needfood.kh.Constructor;

/**
 * Created by admin on 17/07/2017.
 */

public class OrderHisConstructor {
    String id;
    String title;
    String money;
    String status;
    String fullname;
    String fone;
    String address;
    String tymemn;
    String unit;
    String js;

    public OrderHisConstructor(String id, String title, String money, String status, String fullname, String fone, String address, String tymemn, String unit, String js) {
        this.id = id;
        this.title = title;
        this.money = money;
        this.status = status;
        this.fullname = fullname;
        this.fone = fone;
        this.address = address;
        this.tymemn = tymemn;
        this.unit = unit;
        this.js = js;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTymemn() {
        return tymemn;
    }

    public void setTymemn(String tymemn) {
        this.tymemn = tymemn;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getJs() {
        return js;
    }

    public void setJs(String js) {
        this.js = js;
    }
}
