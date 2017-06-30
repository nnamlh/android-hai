package com.congtyhai.model.send;

/**
 * Created by NAM on 12/2/2016.
 */

public class AgencyRequest {

    private String user;

    private String token;

    private String type;

    private String search;


    public AgencyRequest(String user, String token, String type, String search) {
        this.user = user;
        this.token = token;
        this.type = type;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }
}
