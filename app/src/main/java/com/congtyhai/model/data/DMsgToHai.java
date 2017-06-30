package com.congtyhai.model.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by NAM on 11/11/2016.
 */

public class DMsgToHai extends RealmObject{

    @PrimaryKey
    private long id;

    @Required
    private String user ;
    @Required
    private String type;
    @Required
    private String content ;
    @Required
    private String date;


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
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

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
