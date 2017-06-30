package com.congtyhai.model.send;

/**
 * Created by NAM on 11/10/2016.
 */

public class FirebaseReg {

    private String user;

    private String token;

    private String regId;

    private int isUpdate;

    public FirebaseReg(String user, String token, String regId, int isUpdate) {
        this.user = user;
        this.isUpdate = isUpdate;
        this.token = token;
        this.regId = regId;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRegId() {
        return regId;
    }

    public void setRegId(String regId) {
        this.regId = regId;
    }

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }
}
