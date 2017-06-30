package com.congtyhai.model.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

public class NotificationInfo extends RealmObject {

    @PrimaryKey
    @Required
    private String id;

    private int read;

    public int getRead() {
        return read;
    }

    public void setRead(int read) {
        this.read = read;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
