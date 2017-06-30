package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 3/15/2017.
 */

public class ReceiverResult {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("receivers")
    private AgencyInfo[] receivers;

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

    public AgencyInfo[] getReceivers() {
        return receivers;
    }

    public void setReceivers(AgencyInfo[] receivers) {
        this.receivers = receivers;
    }
}
