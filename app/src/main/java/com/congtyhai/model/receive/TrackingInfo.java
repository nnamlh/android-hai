package com.congtyhai.model.receive;

import com.google.gson.annotations.SerializedName;

/**
 * Created by NAM on 11/19/2016.
 */

public class TrackingInfo {

    @SerializedName("name")
    private String name;

    @SerializedName("status")
    private String status ;

    @SerializedName("importTime")
    private String importTime ;

    @SerializedName("exportTime")
    private String exportTime;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImportTime() {
        return importTime;
    }

    public void setImportTime(String importTime) {
        this.importTime = importTime;
    }

    public String getExportTime() {
        return exportTime;
    }

    public void setExportTime(String exportTime) {
        this.exportTime = exportTime;
    }
}
