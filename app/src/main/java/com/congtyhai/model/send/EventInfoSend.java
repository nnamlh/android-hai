package com.congtyhai.model.send;

/**
 * Created by NAM on 11/16/2016.
 */

public class EventInfoSend {


    private String eventId;

    private String user;

    private String token;


    public EventInfoSend(String user, String token, String eventId) {
        this.user = user;
        this.token = token;
        this.eventId = eventId;
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

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }
}
