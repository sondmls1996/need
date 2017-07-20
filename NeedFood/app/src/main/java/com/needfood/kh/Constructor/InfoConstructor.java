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

    public InfoConstructor() {
    }

    public InfoConstructor(String fullname, String email, String fone, String pass, String address, String id, String accesstoken) {
        this.fullname = fullname;
        this.email = email;
        this.fone = fone;
        this.pass = pass;
        this.address = address;
        this.id = id;
        this.accesstoken = accesstoken;
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
}
