package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 11/16/2016.
 */

public class ResultEvent {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("events")
    private ResultEventInfo[] events;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public ResultEventInfo[] getEvents() {
        return events;
    }

    public void setEvents(ResultEventInfo[] events) {
        this.events = events;
    }
}
