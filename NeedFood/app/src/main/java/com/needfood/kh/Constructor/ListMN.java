package com.needfood.kh.Constructor;

/**
 * Created by Vi on 6/29/2017.
 */

public class ListMN {

    public String idmn;
    public String mn;

    public ListMN(String idmn, String mn) {
        this.idmn = idmn;
        this.mn = mn;
    }

    public ListMN() {

    }

    public String getIdmn() {
        return idmn;
    }

    public void setIdmn(String idmn) {
        this.idmn = idmn;
    }

    public String getMn() {
        return mn;
    }

    public void setMn(String mn) {
        this.mn = mn;
    }
}
