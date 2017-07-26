package com.nf.vi.needfoodshipper.Constructor;

/**
 * Created by Minh Nhat on 7/26/2017.
 */

public class ListviewContructor {
    public String sanpham;
    public String soluong;
    public String dongia;
    public String tinhtien;

    public ListviewContructor() {
    }

    public ListviewContructor(String sanpham, String soluong, String dongia, String tinhtien) {
        this.sanpham = sanpham;
        this.soluong = soluong;
        this.dongia = dongia;
        this.tinhtien = tinhtien;
    }

    public String getSanpham() {
        return sanpham;
    }

    public void setSanpham(String sanpham) {
        this.sanpham = sanpham;
    }

    public String getSoluong() {
        return soluong;
    }

    public void setSoluong(String soluong) {
        this.soluong = soluong;
    }

    public String getDongia() {
        return dongia;
    }

    public void setDongia(String dongia) {
        this.dongia = dongia;
    }

    public String getTinhtien() {
        return tinhtien;
    }

    public void setTinhtien(String tinhtien) {
        this.tinhtien = tinhtien;
    }
}
