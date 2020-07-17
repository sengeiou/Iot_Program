package com.vtech.vhealth.function.bean;

import org.litepal.crud.LitePalSupport;

public class HealthRecord extends LitePalSupport {
    private String data;

    private int status;

    public String userId;

    //原始数据
    private int isbp; //收缩压
    private int idbp; //舒张压
    private int ihrate;//心率

    public String creatTime;//创建时间 暂定

    public int getIsbp() {
        return isbp;
    }

    public void setIsbp(int isbp) {
        this.isbp = isbp;
    }

    public int getIdbp() {
        return idbp;
    }

    public void setIdbp(int idbp) {
        this.idbp = idbp;
    }

    public int getIhrate() {
        return ihrate;
    }

    public void setIhrate(int ihrate) {
        this.ihrate = ihrate;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

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
        return "HealthRecord{" +
                "data='" + data + '\'' +
                ", status=" + status +
                ", userId='" + userId + '\'' +
                ", isbp=" + isbp +
                ", idbp=" + idbp +
                ", ihrate=" + ihrate +
                ", creatTime='" + creatTime + '\'' +
                '}';
    }
}
