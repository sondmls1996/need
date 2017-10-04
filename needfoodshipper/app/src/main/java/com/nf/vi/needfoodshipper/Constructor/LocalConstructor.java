package com.nf.vi.needfoodshipper.Constructor;

/**
 * Created by admin on 31/07/2017.
 */

public class LocalConstructor {
    double lat;
    double lo;

    public LocalConstructor() {
    }

    public LocalConstructor(double lat, double lo) {
        this.lat = lat;
        this.lo = lo;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLo() {
        return lo;
    }

    public void setLo(double lo) {
        this.lo = lo;
    }
}
