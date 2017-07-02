package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HAI on 7/3/2017.
 */

public class CheckUserLoginResult {

    @SerializedName("id")
    private String id ;
    @SerializedName("msg")
    private String msg ;
    @SerializedName("name")
    private String name ;
    @SerializedName("store")
    private String store ;
    @SerializedName("code")
    private String code ;
    @SerializedName("phone")
    private String phone ;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
