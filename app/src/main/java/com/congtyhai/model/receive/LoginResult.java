package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;

import java.util.Arrays;

/**
 * Created by NAM on 10/19/2016.
 */

public class LoginResult {

    @SerializedName("id")
    private String id;

    @SerializedName("msg")
    private String msg;

    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private String user;

    @SerializedName("function")
    private String[] function;

    @SerializedName("Role")
    private String Role;

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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getFunction() {
        JSONArray mJSONArray = new JSONArray(Arrays.asList(function));
        return  mJSONArray.toString();
    }

    public void setFunction(String[] function) {
        this.function = function;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
