package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 11/16/2016.
 */

public class ResultEventInfo {

    @SerializedName("eid")
    private String eid ;

    @SerializedName("ename")
    private String ename ;

    @SerializedName("etime")
    private String etime ;

    @SerializedName("eimage")
    private String eimage ;


    public String getEid() {
        return eid;
    }

    public void setEid(String eid) {
        this.eid = eid;
    }

    public String getEname() {
        return ename;
    }

    public void setEname(String ename) {
        this.ename = ename;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getEimage() {
        return eimage;
    }

    public void setEimage(String eimage) {
        this.eimage = eimage;
    }

}
