package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 6/30/2017.
 */

public class ReceiveInfo {


    @SerializedName("code")
    private String code ;
    @SerializedName("name")
    private String name ;

    @SerializedName("type")
    private String type;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
