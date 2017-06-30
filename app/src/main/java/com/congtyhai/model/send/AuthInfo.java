package com.congtyhai.model.send;

/**
 * Created by NAM on 11/11/2016.
 */

public class AuthInfo {

    private String user;

    private String token;


    public AuthInfo(String user, String token) {
        this.user = user;
        this.token = token;
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
}
