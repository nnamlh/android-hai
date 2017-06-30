package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 12/10/2016.
 */

public class GeneralInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("code")
    private String code;

    @SerializedName("status")
    private String status;

    @SerializedName("success")
    private int success;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }
}
