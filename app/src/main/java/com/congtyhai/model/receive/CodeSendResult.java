package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by NAM on 11/16/2016.
 */

public class CodeSendResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("codes")
    private List<GeneralInfo> codes;

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


    public List<GeneralInfo> getCodes() {
        return codes;
    }

    public void setCodes(List<GeneralInfo> codes) {
        this.codes = codes;
    }
}
