package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 12/2/2016.
 */

public class AgencyResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("agences")
    private AgencyInfo[] agences;

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

    public AgencyInfo[] getAgences() {
        return agences;
    }

    public void setAgences(AgencyInfo[] agences) {
        this.agences = agences;
    }
}
