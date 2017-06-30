package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

/**
 * Created by NAM on 1/17/2017.
 */

public class ProductFunction {

    @SerializedName("id")
    private String id ;

    @SerializedName("msg")
    private String msg ;

    @SerializedName("function")
    private  String[] function;

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

    public String getFunction() {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(function));
        return  mJSONArray.toString();
    }

    public void setFunction(String[] function) {
        this.function = function;
    }
}
