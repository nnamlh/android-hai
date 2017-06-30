package com.congtyhai.model;

/**
 * Created by NAM on 11/11/2016.
 */

public class MsgType {

    private String code;
    private String name;

    public MsgType(String code, String name) {
        this.code = code;
        this.name = name;
    }

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

    @Override
    public String toString() {
        return this.name;
    }
}
