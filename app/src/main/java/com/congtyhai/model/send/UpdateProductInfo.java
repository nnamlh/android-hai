package com.congtyhai.model.send;


/**
 * Created by NAM on 11/11/2016.
 */

public class UpdateProductInfo {

    private String user ;
    private String token ;
    private String[] products;

    private String receiver;

    private String status ;


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

    public String[] getProducts() {
        return products;
    }

    public void setProducts(String[] products) {
        this.products = products;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
