package com.congtyhai.model;

/**
 * Created by NAM on 10/11/2016.
 */

public class ActionInfo {

    private String title;
    private String content;
    private int image;
    private String actiion;
    private int count;
    private boolean first;

    public ActionInfo(String title, String content, int image, String actiion, int count) {
        this.title = title;
        this.content = content;
        this.image = image;
        this.actiion = actiion;
        this.count = count;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }


    public String getActiion() {
        return actiion;
    }

    public void setActiion(String actiion) {
        this.actiion = actiion;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }
}
