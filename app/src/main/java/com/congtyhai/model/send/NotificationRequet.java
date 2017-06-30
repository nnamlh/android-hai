package com.congtyhai.model.send;

/**
 * Created by NAM on 12/19/2016.
 */

public class NotificationRequet {

    private String user;

    private String token;


    private int pageno;


    public NotificationRequet(String user, String token, int pageno) {
        this.user = user;
        this.token = token;
        this.pageno = pageno;
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

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }
}
