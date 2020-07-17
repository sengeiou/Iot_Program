package com.vtech.vhealth.function.bean;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

public class HealthUploadBean extends LitePalSupport {
    @Column(unique = false,defaultValue = "")
    private String data;
    @Column(unique = false,defaultValue = "0")
    private int status;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "HealthUploadBean{" +
                "data='" + data + '\'' +
                ", status=" + status +
                '}';
    }
}
