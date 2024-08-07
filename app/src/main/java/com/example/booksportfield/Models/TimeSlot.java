package com.example.booksportfield.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class TimeSlot implements Serializable {
    private String id_timeSlot;
    private String booking_date;
    private String start_time;
    private String end_time;
    private String status;


    //Constructor
    public TimeSlot(String booking_date, String start_time, String end_time, String status) {
        this.booking_date = booking_date;
        this.start_time = start_time;
        this.end_time = end_time;
        if(status == "Chưa đặt")
            this.status = "chuadat";
        else if(status == "Đặt")
            this.status = "dat";
    }

    public TimeSlot(){}

    //Get set
    public void setId_timeSlot(String id_timeSlot) {
        this.id_timeSlot = id_timeSlot;
    }

    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setStatusUpdate(String status)
    {
        if(status == "Chưa đặt")
            this.status = "chuadat";
        else if(status == "Đặt")
            this.status = "dat";
    }


    public String getId_timeSlot() {
        return id_timeSlot;
    }

    public String toString(){
        if(this.booking_date == null || this.status =="Đặt")
            return null;
        return this.booking_date + "\n" + "(" + this.start_time + "h -" + this.end_time + "h)";
    }

    public String getBooking_date() {
        return booking_date;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getStatus() {
        return status;
    }


    //Operation

    public Map<String, Object> toMap()
    {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id_timeSlot",id_timeSlot);
        result.put("booking_date", booking_date);
        result.put("start_time", start_time);
        result.put("end_time", end_time);
        result.put("status", status);
        return result;
    }

}
