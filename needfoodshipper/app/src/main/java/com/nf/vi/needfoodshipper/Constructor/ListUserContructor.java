package com.nf.vi.needfoodshipper.Constructor;

/**
 * Created by Minh Nhat on 7/4/2017.
 */

public class ListUserContructor {
    public String id;
    public String code;
    public String fullName;
    public String fone;
    public String email;
    public String address;
    public String birthday;
    public String accessToken;
    public String pass;
    public String skype;
    public String facebook;

    public String description;

    public ListUserContructor(String id, String code, String fullName, String fone, String email, String address, String birthday, String accessToken, String pass, String skype, String facebook, String description) {
        this.id = id;
        this.code = code;
        this.fullName = fullName;
        this.fone = fone;
        this.email = email;
        this.address = address;
        this.birthday = birthday;
        this.accessToken = accessToken;
        this.pass = pass;
        this.skype = skype;
        this.facebook = facebook;
        this.description = description;
    }

    public ListUserContructor() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getSkype() {
        return skype;
    }

    public void setSkype(String skype) {
        this.skype = skype;
    }

    public String getFacebook() {
        return facebook;
    }

    public void setFacebook(String facebook) {
        this.facebook = facebook;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
