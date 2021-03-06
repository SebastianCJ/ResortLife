package com.logvit.resortlife;

/**
 * Created by Gatsu on 6/29/2017.
 */

public class Point {


    public double longitude = 0f;
    public double latitude = 0f;
    public String description;
    public float x, y = 0;

    public Point(double lat, double lon, String desc) {
        this.latitude = lat;
        this.longitude = lon;
        this.description = desc;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getDescription() {
        return description;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
