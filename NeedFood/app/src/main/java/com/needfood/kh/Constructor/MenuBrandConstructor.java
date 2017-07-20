package com.needfood.kh.Constructor;

/**
 * Created by Vi on 5/8/2017.
 */

public class MenuBrandConstructor {
    public String namemn;
    public String imgmenu;
    public String giamn;
    public String giacu;
    public String mn;
    public String id;
    public String unit;

    public MenuBrandConstructor(String namemn, String imgmenu, String giamn, String giacu, String mn, String id, String unit) {
        this.namemn = namemn;
        this.imgmenu = imgmenu;
        this.giamn = giamn;
        this.giacu = giacu;
        this.mn = mn;
        this.id = id;
        this.unit = unit;
    }

    public String getNamemn() {
        return namemn;
    }

    public void setNamemn(String namemn) {
        this.namemn = namemn;
    }

    public String getImgmenu() {
        return imgmenu;
    }

    public void setImgmenu(String imgmenu) {
        this.imgmenu = imgmenu;
    }

    public String getGiamn() {
        return giamn;
    }

    public void setGiamn(String giamn) {
        this.giamn = giamn;
    }

    public String getGiacu() {
        return giacu;
    }

    public void setGiacu(String giacu) {
        this.giacu = giacu;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
