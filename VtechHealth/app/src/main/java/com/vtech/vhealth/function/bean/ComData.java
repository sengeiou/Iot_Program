package com.vtech.vhealth.function.bean;

public class ComData {

    private String userName;
    private String deviceId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    @Override
    public String toString() {
        return "ComData{" +
                "userName='" + userName + '\'' +
                ", deviceId='" + deviceId + '\'' +
                '}';
    }
}
