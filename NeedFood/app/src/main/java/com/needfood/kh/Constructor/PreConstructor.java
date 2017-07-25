package com.needfood.kh.Constructor;

public class PreConstructor {
    public String namepre;
    public String quanpre;
    public String totalpre;
    public String moneypre;

    public String getNamepre() {
        return namepre;
    }

    public void setNamepre(String namepre) {
        this.namepre = namepre;
    }

    public String getQuanpre() {
        return quanpre;
    }

    public void setQuanpre(String quanpre) {
        this.quanpre = quanpre;
    }

    public String getTotalpre() {
        return totalpre;
    }

    public void setTotalpre(String totalpre) {
        this.totalpre = totalpre;
    }

    public String getMoneypre() {
        return moneypre;
    }

    public void setMoneypre(String moneypre) {
        this.moneypre = moneypre;
    }

    public PreConstructor(String namepre, String quanpre, String totalpre, String moneypre) {
        this.namepre = namepre;
        this.quanpre = quanpre;
        this.totalpre = totalpre;
        this.moneypre = moneypre;
    }
}
