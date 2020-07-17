package com.vtech.check.function.bean;

import org.litepal.crud.LitePalSupport;

public class EcgBean extends LitePalSupport {
    private String ecgData;

    public String getEcgData() {
        return ecgData;
    }

    public void setEcgData(String ecgData) {
        this.ecgData = ecgData;
    }
}
