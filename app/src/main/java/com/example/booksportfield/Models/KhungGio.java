package com.example.booksportfield.Models;

import java.io.Serializable;

public class KhungGio implements Serializable {
    private String start_time;
    private String end_time;
    private String status;

    public KhungGio() {
    }

    public KhungGio(String start_time, String end_time, String status) {
        this.start_time = start_time;
        this.end_time = end_time;
        this.status = status;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
