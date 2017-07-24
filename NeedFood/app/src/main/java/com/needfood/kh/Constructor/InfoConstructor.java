package com.needfood.kh.Constructor;

/**
 * Created by admin on 11/07/2017.
 */

public class InfoConstructor {
    String fullname;
    String email;
    String fone;
    String pass;
    String address;
    String id;
    String accesstoken;
    String coin;
    String type;

    public InfoConstructor() {
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccesstoken() {
        return accesstoken;
    }

    public void setAccesstoken(String accesstoken) {
        this.accesstoken = accesstoken;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public InfoConstructor(String fullname, String email, String fone, String pass, String address, String id, String accesstoken, String coin, String type) {
        this.fullname = fullname;
        this.email = email;
        this.fone = fone;
        this.pass = pass;
        this.address = address;
        this.id = id;
        this.accesstoken = accesstoken;
        this.coin = coin;
        this.type = type;
    }
}
