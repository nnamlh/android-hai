package com.congtyhai.model;

/**
 * Created by computer on 6/22/2017.
 */

public class HaiLocation {

    private double latitude;
    private double longitude;

    public  HaiLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
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
}
