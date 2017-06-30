package com.congtyhai.model.receive;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * Created by NAM on 11/10/2016.
 */

public class ResultTopic {
    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("ecount")
    private int ecount;

    @SerializedName("topics")
    private String[] topics;

    @SerializedName("function")
    private String[] function;

    @SerializedName("agencies")
    private AgencyInfo[] agencies;

    @SerializedName("recivers")
    private AgencyInfo[] recivers;


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

    public String[] getTopics() {
        return topics;
    }

    public void setTopics(String[] topics) {
        this.topics = topics;
    }

    public int getEcount() {
        return ecount;
    }

    public void setEcount(int ecount) {
        this.ecount = ecount;
    }

    public String getFunction() {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(function));
        return  mJSONArray.toString();
    }

    public void setFunction(String[] function) {
        this.function = function;
    }



    public String getAgencies() {

        Gson gson = new Gson();

        return gson.toJson(agencies).toString();
    }

    public void setAgencies(AgencyInfo[] agencies) {
        this.agencies = agencies;
    }

    public String getRecivers() {
        Gson gson = new Gson();
        return gson.toJson(recivers).toString();
    }

    public void setRecivers(AgencyInfo[] recivers) {
        this.recivers = recivers;
    }
}
