package com.congtyhai.model.send;

/**
 * Created by NAM on 3/15/2017.
 */

public class ReceiverRequest {

    private String user;

    private String token;

    private String search;


    public ReceiverRequest(String user, String token, String search) {
        this.user = user;
        this.token = token;
        this.search = search;
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

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

}
