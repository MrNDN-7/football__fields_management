package com.example.booksportfield.Models;

import java.io.Serializable;

public class History implements Serializable {
    private String tenSan;
    private String ngayDat;
    private int loaiSan;
    private double rating;

    private String user_id;
    private String ngayHetHan;
    private String booking_id;

    private String id_time_slot;
    private String id_field;

    public String getId_time_slot() {
        return id_time_slot;
    }

    public void setId_time_slot(String id_time_slot) {
        this.id_time_slot = id_time_slot;
    }

    public String getId_field() {
        return id_field;
    }

    public void setId_field(String id_field) {
        this.id_field = id_field;
    }

    public String getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(String booking_id) {
        this.booking_id = booking_id;
    }

    public String getNgayHetHan() {
        return ngayHetHan;
    }

    public void setNgayHetHan(String ngayHetHan) {
        this.ngayHetHan = ngayHetHan;
    }

    public History() {
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTenSan() {
        return tenSan;
    }

    public void setTenSan(String tenSan) {
        this.tenSan = tenSan;
    }

    public String getNgayDat() {
        return ngayDat;
    }

    public void setNgayDat(String ngayDat) {
        this.ngayDat = ngayDat;
    }

    public int getLoaiSan() {
        return loaiSan;
    }

    public void setLoaiSan(int loaiSan) {
        this.loaiSan = loaiSan;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
