package com.congtyhai.model.data;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by HAI on 7/2/2017.
 */

public class DHistoryProductScan extends RealmObject {

    @PrimaryKey
    private long id;

    private String time;
    private String status;
    private String receive;
    private String screen;
    private String agency;
    private String product;
    private String productResult;
    private String countSuccess;
    private String countFail;
    private String quantity;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReceive() {
        return receive;
    }

    public void setReceive(String receive) {
        this.receive = receive;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        this.agency = agency;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getProductResult() {
        return productResult;
    }

    public void setProductResult(String productResult) {
        this.productResult = productResult;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCountSuccess() {
        return countSuccess;
    }

    public void setCountSuccess(String countSuccess) {
        this.countSuccess = countSuccess;
    }

    public String getCountFail() {
        return countFail;
    }

    public void setCountFail(String countFail) {
        this.countFail = countFail;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
