package com.example.attemp1.model;

import java.util.Date;

public class Hike {
    private Integer id;
    private String name;
    private String location;
    private Date date;
    private boolean parkingAvailability;
    private double length;
    private String difficulty;
    private String description;

    // Constructors, getters, and setters

    // Constructor
    public Hike(int id, String name, String location, Date date, boolean parkingAvailability,
                double length, String difficulty, String description) {
        this.id = id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailability = parkingAvailability;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
    }
    public Hike(String name, String location, Date date, boolean parkingAvailability,
                double length, String difficulty, String description) {
        this.id = null;
        this.name = name;
        this.location = location;
        this.date = date;
        this.parkingAvailability = parkingAvailability;
        this.length = length;
        this.difficulty = difficulty;
        this.description = description;
    }

    public Hike(){
        this.id = null;
        this.name = null;
        this.location = null;
        this.date = null;
        this.parkingAvailability = false;
        this.length = 0;
        this.difficulty = null;
        this.description = null;
    }

    // Getters and setters for each field

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isParkingAvailability() {
        return parkingAvailability;
    }

    public void setParkingAvailability(boolean parkingAvailability) {
        this.parkingAvailability = parkingAvailability;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}