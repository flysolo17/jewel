package com.example.trash_scan.firebase.models;

import java.util.List;

public class Destinations {
    String destinationID;
    String collectorID;
    List<String> listAddresses;
    int nowCollecting;
    long timestamp;
    public static String TABLE_NAME = "Destinations";
    public Destinations() {

    }

    public Destinations(String destinationID, String collectorID, List<String> listAddresses, int nowCollecting, long timestamp) {
        this.destinationID = destinationID;
        this.collectorID = collectorID;
        this.listAddresses = listAddresses;
        this.nowCollecting = nowCollecting;
        this.timestamp = timestamp;
    }

    public String getDestinationID() {
        return destinationID;
    }

    public void setDestinationID(String destinationID) {
        this.destinationID = destinationID;
    }

    public String getCollectorID() {
        return collectorID;
    }

    public void setCollectorID(String collectorID) {
        this.collectorID = collectorID;
    }



    public List<String> getListAddresses() {
        return listAddresses;
    }

    public void setListAddresses(List<String> listAddresses) {
        this.listAddresses = listAddresses;
    }

    public int getNowCollecting() {
        return nowCollecting;
    }

    public void setNowCollecting(int nowCollecting) {
        this.nowCollecting = nowCollecting;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}