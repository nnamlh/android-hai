package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 11/16/2016.
 */

public class ResultEventDetail {

    @SerializedName("eid")
    private String eid ;

    @SerializedName("ename")
    private String ename ;

    @SerializedName("etime")
    private String etime ;

    @SerializedName("eimage")
    private String eimage ;

    @SerializedName("id")
    private String id ;

    @SerializedName("msg")
    private String msg ;

    @SerializedName("edescribe")
    private String edescribe ;

    @SerializedName("areas")
    private String[] areas;

    @SerializedName("awards")
    private AwardInfo[] awards ;

    @SerializedName("products")
    private EventProduct[] products;

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

    public String getEdescribe() {
        return edescribe;
    }

    public void setEdescribe(String edescribe) {
        this.edescribe = edescribe;
    }

    public String[] getAreas() {
        return areas;
    }

    public void setAreas(String[] areas) {
        this.areas = areas;
    }

    public AwardInfo[] getAwards() {
        return awards;
    }

    public void setAwards(AwardInfo[] awards) {
        this.awards = awards;
    }

    public EventProduct[] getProducts() {
        return products;
    }

    public void setProducts(EventProduct[] products) {
        this.products = products;
    }
}
