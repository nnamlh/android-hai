package com.congtyhai.model.send;

/**
 * Created by computer on 6/22/2017.
 */

public class StaffHelpRequest {

    private String user ;
    private String token ;
    private String[] products;

    private double latitude;

    private double longitude ;

    private String agency;

    private int nearAgency ;


    public StaffHelpRequest (String user, String token, String[] products, double latitude, double longitude, String agency, int nearAgency) {
        this.user = user;
        this.token = token;
        this.products = products;
        this.latitude = latitude;
        this.longitude = longitude;
        this.agency = agency;
        this.nearAgency = nearAgency;
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

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
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

    public int getNearAgency() {
        return nearAgency;
    }

    public void setNearAgency(int nearAgency) {
        this.nearAgency = nearAgency;
    }
}
