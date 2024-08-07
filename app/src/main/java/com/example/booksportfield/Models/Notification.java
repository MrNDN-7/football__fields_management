package com.example.booksportfield.Models;

import java.util.Date;

public class Notification {
    private String message_content;
    private Date message_date;
    private String receiver_id;
    private String sender_id;

    public String getMessage_content() {
        return message_content;
    }

    public void setMessage_content(String message_content) {
        this.message_content = message_content;
    }

    public Date getMessage_date() {
        return message_date;
    }

    public void setMessage_date(Date message_date) {
        this.message_date = message_date;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public Notification() {
    }

    public Notification(String message_content, Date message_date, String receiver_id) {
        this.message_content = message_content;
        this.message_date = message_date;
        this.receiver_id = receiver_id;
    }
}
