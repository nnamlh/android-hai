package com.congtyhai.model;

/**
 * Created by computer on 6/21/2017.
 */

public class ProductChooseFuncInfo {

    private String title;
    private int image;
    private String code;

    public ProductChooseFuncInfo(String code, String title, int image){
        this.code = code;
        this.title = title;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
