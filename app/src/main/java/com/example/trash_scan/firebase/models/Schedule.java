package com.example.trash_scan.firebase.models;

import java.util.List;

public class Schedule {
    String id;
    String gsoID;
    String collectorID;
    List<String> days;
    Time startTime;
    List<String> routes;
    public static String TABLE_NAME= "Schedule";
    public static String COLLECTOR_ID= "collectorID";
    public Schedule() {
    }

    public Schedule(String id, String gsoID, String collectorID, List<String> days, Time startTime, List<String> routes) {
        this.id = id;
        this.gsoID = gsoID;
        this.collectorID = collectorID;
        this.days = days;
        this.startTime = startTime;
        this.routes = routes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGsoID() {
        return gsoID;
    }

    public void setGsoID(String gsoID) {
        this.gsoID = gsoID;
    }

    public String getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(String collectorID) {
        this.collectorID = collectorID;
    }

    public List<String> getDays() {
        return days;
    }

    public void setDays(List<String> days) {
        this.days = days;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public List<String> getRoutes() {
        return routes;
    }

    public void setRoutes(List<String> routes) {
        this.routes = routes;
    }
}
