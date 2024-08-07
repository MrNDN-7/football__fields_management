package com.example.booksportfield.Models;

import java.io.Serializable;
import java.util.ArrayList;

public class Field implements Serializable {

    private String nodeID;
    private String address;
    private String description;
    private String field_owner;
    private int field_type;
    private ArrayList<String> image;
    private String name;
    private int price;
    private double rating;

    private String field_id;

    public String getNodeID() {
        return nodeID;
    }

    public void setNodeID(String nodeID) {
        this.nodeID = nodeID;
    }

    public String getField_id() {
        return field_id;
    }

    public void setField_id(String field_id) {
        this.field_id = field_id;
    }

    private ArrayList<TimeSlot> timeslot;

    public String getAddress() {
        return address;
    }

    public ArrayList<TimeSlot> getTimeslot() {
        return timeslot;
    }

    public String getTimeById(String id)
    {
        for(TimeSlot timeSlot : this.getTimeslot())
        {
            if(timeSlot.getId_timeSlot().equals(id))
                return timeSlot.toString();
        }
        return null;
    }
    public void setTimeslots(ArrayList<TimeSlot> timeslots) {
        this.timeslot = timeslots;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getField_owner() {
        return field_owner;
    }

    public void setField_owner(String field_owner) {
        this.field_owner = field_owner;
    }

    public int getField_type() {
        return field_type;
    }

    public void setField_type(int field_type) {
        this.field_type = field_type;
    }

    public ArrayList<String> getImage() {
        return image;
    }

    public void setImage(ArrayList<String> image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Field() {
    }

    public Field(String address, String description, String field_owner, int field_type, ArrayList<String> image, String name, int price, float rating) {
        this.address = address;
        this.description = description;
        this.field_owner = field_owner;
        this.field_type = field_type;
        this.image = image;
        this.name = name;
        this.price = price;
        this.rating = rating;
    }
}
