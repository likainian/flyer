package com.flyer.chat.activity.home.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by mike.li on 2020/4/29.
 */
public class SignEntity extends BmobObject {
    private String phoneNumber;
    private double latitude;
    private double longitude;
    private String location;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "SignEntity{" +
                "phoneNumber='" + phoneNumber + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location='" + location + '\'' +
                '}';
    }
}
