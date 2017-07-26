package com.needfood.kh.Constructor;

import com.needfood.kh.News.News;

import java.util.Comparator;

/**
 * Created by Vi on 4/24/2017.
 */

public class NewsConstructor {
    public String linkimg;
    public String idprd;
    public String idsl;
    public String name;
    public String nameauth;
    public String price;
    public String imga;
    public String price2;
    public String vt;
    public String dv;
    public String mn;

    public NewsConstructor(String linkimg, String idprd, String idsl, String name, String nameauth, String price, String imga, String price2, String vt, String dv, String mn) {
        this.linkimg = linkimg;
        this.idprd = idprd;
        this.idsl = idsl;
        this.name = name;
        this.nameauth = nameauth;
        this.price = price;
        this.imga = imga;
        this.price2 = price2;
        this.vt = vt;
        this.dv = dv;
        this.mn = mn;
    }

    public String getLinkimg() {
        return linkimg;
    }

    public void setLinkimg(String linkimg) {
        this.linkimg = linkimg;
    }

    public String getIdprd() {
        return idprd;
    }

    public void setIdprd(String idprd) {
        this.idprd = idprd;
    }

    public String getIdsl() {
        return idsl;
    }

    public void setIdsl(String idsl) {
        this.idsl = idsl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameauth() {
        return nameauth;
    }

    public void setNameauth(String nameauth) {
        this.nameauth = nameauth;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImga() {
        return imga;
    }

    public void setImga(String imga) {
        this.imga = imga;
    }

    public String getPrice2() {
        return price2;
    }

    public void setPrice2(String price2) {
        this.price2 = price2;
    }

    public String getVt() {
        return vt;
    }

    public void setVt(String vt) {
        this.vt = vt;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }
}
