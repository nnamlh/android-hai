package com.congtyhai.model.send;

/**
 * Created by NAM on 10/25/2016.
 */

public class CheckIn {

    private String comment;
    private String image;

    private String user;

    private double latitude;

    private double longitude;

    private String token;

    private String date;

    private String agency;

    public CheckIn(String user,String token, String comment, String image, double latitude, double longitude,String date, String agency) {
        this.user = user;
        this.comment = comment;
        this.image = image;
        this.latitude = latitude;
        this.longitude = longitude;
        this.token = token;
        this.date = date;
        this.agency = agency;
    }


    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

}
