package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by NAM on 12/19/2016.
 */

public class NotificationResult {

    @SerializedName("pagemax")
    private int pagemax;

    @SerializedName("pageno")
    private int pageno;

    @SerializedName("id")
    private String id;
    @SerializedName("msg")
    private String msg;

    @SerializedName("notifications")
    private List<NotificationResultInfo> notifications;

    public int getPageno() {
        return pageno;
    }

    public void setPageno(int pageno) {
        this.pageno = pageno;
    }

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

    public List<NotificationResultInfo> getNotifications() {
        return notifications;
    }

    public void setNotifications(List<NotificationResultInfo> notifications) {
        this.notifications = notifications;
    }

    public int getPagemax() {
        return pagemax;
    }

    public void setPagemax(int pagemax) {
        this.pagemax = pagemax;
    }
}
