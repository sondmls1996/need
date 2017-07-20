package com.needfood.kh.Constructor;

/**
 * Created by Vi on 7/14/2017.
 */

public class SearchConstructor {
    public String linkimg;
    public String name;
    public String id;
    public String gia;
    public String donvi;
    public String unit;

    public SearchConstructor(String linkimg, String name, String id, String gia, String donvi, String unit) {
        this.linkimg = linkimg;
        this.name = name;
        this.id = id;
        this.gia = gia;
        this.donvi = donvi;
        this.unit = unit;
    }

    public String getLinkimg() {
        return linkimg;
    }

    public void setLinkimg(String linkimg) {
        this.linkimg = linkimg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    public String getDonvi() {
        return donvi;
    }

    public void setDonvi(String donvi) {
        this.donvi = donvi;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
