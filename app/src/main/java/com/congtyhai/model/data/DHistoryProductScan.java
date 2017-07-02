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
    private int isUpdate;
    private String titleScreen;
    private int isNear;

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

    public int getIsUpdate() {
        return isUpdate;
    }

    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitleScreen() {
        return titleScreen;
    }

    public void setTitleScreen(String titleScreen) {
        this.titleScreen = titleScreen;
    }

    public int getIsNear() {
        return isNear;
    }

    public void setIsNear(int isNear) {
        this.isNear = isNear;
    }
}
