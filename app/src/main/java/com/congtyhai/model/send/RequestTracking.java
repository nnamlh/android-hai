package com.congtyhai.model.send;

/**
 * Created by NAM on 11/19/2016.
 */

public class RequestTracking {

    private String user;

    private String token;

    private String code;


    public RequestTracking(String user, String token, String code) {
        this.user = user;
        this.token = token;
        this.code = code;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
