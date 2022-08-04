package com.example.trash_scan.firebase.models;

public class Time {
    int hour;
    int minute;
    String meridiem;

    public Time() {
    }

    public Time(int hour, int minute, String meridiem) {
        this.hour = hour;
        this.minute = minute;
        this.meridiem = meridiem;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getMeridiem() {
        return meridiem;
    }

    public void setMeridiem(String meridiem) {
        this.meridiem = meridiem;
    }
}
