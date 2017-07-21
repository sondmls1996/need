package com.needfood.kh.Constructor;

/**
 * Created by admin on 20/07/2017.
 */

public class MapConstructor {
    String id;
    String brandname;
    String fullname;
    String fone;
    String address;
    String lat;
    String lo;

    public MapConstructor() {
    }

    public MapConstructor(String id, String brandname, String fullname, String fone, String address, String lat, String lo) {
        this.id = id;
        this.brandname = brandname;
        this.fullname = fullname;
        this.fone = fone;
        this.address = address;
        this.lat = lat;
        this.lo = lo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBrandname() {
        return brandname;
    }

    public void setBrandname(String brandname) {
        this.brandname = brandname;
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

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLo() {
        return lo;
    }

    public void setLo(String lo) {
        this.lo = lo;
    }
}
