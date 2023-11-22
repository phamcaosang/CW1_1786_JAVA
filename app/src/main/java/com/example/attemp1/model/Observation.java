package com.example.attemp1.model;

import java.util.Date;

public class Observation {
    private Integer hikeID;
    private Integer id;
    private String observation;
    private Date time;
    private String additionalComments;

    // Constructors, getters, and setters

    // Constructor
    public Observation(){
        this.id = null;
        this.hikeID = null;
        this.observation = null;
        this.time = null;
        this.additionalComments = null;
    }
    public Observation(String observation,  String additionalComments, int hikeID) {
        this.id = null;
        this.hikeID = hikeID;
        this.observation = observation;
        this.time = new Date();;
        this.additionalComments = additionalComments;
    }

    public Observation(int id,  String observation, Date time, String additionalComments, int hikeID) {
        this.id = id;
        this.observation = observation;
        this.time = time;
        this.additionalComments = additionalComments;
        this.hikeID = hikeID;
    }

    // Getters and setters for each field

    public Integer getHikeID() {
        return hikeID;
    }

    public void setHikeID(Integer hikeID) {
        this.hikeID = hikeID;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }
}