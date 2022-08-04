package com.example.trash_scan.firebase.models;

public class Collector {
    String id;
    String firstName;
    String lastName;
    String plateNumber;
    String email;
    public static String TABLE_NAME = "Collector";
    public Collector() {
    }

    public Collector(String id, String firstName, String lastName,String plateNumber, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.plateNumber = plateNumber;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
