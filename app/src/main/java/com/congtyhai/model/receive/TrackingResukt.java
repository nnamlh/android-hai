package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 11/19/2016.
 */

public class TrackingResukt {


    @SerializedName("code")
    private String code;

    @SerializedName("tracking")
    private TrackingInfo[] tracking;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;


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

    public TrackingInfo[] getTracking() {
        return tracking;
    }

    public void setTracking(TrackingInfo[] tracking) {
        this.tracking = tracking;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
