package com.congtyhai.model.send;

/**
 * Created by computer on 6/22/2017.
 */

public class CheckLocationRequest {

    private String user ;
    private String token ;
    private double latitude;

    private double longitude ;

    private String agency;

    public CheckLocationRequest(String user, String token, String agency, double latitude, double longitude) {
        this.user = user;
        this.token = token;
        this.agency = agency;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }
}
