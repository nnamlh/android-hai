package com.congtyhai.model.send;

/**
 * Created by NAM on 11/11/2016.
 */

public class MsgToHai {



    private String user ;
    private String token ;

    private String type;

    private String content ;


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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
