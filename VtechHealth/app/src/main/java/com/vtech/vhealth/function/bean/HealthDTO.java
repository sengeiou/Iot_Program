package com.vtech.vhealth.function.bean;

public class HealthDTO {
    private String uid;
    private String deviceId;
    private int iSBP;
    private int iDBP;
    private int iHRate;
    //脉搏
    private Integer [] mPluse;
    //心电
    private Integer [] mEcg ;
    private int calibrationStatus;
    private int clearCalibration;

    public int getCalibrationStatus() {
        return calibrationStatus;
    }

    public void setCalibrationStatus(int calibrationStatus) {
        this.calibrationStatus = calibrationStatus;
    }

    public int getClearCalibration() {
        return clearCalibration;
    }

    public void setClearCalibration(int clearCalibration) {
        this.clearCalibration = clearCalibration;
    }

    public int getiSBP() {
        return iSBP;
    }

    public void setiSBP(int iSBP) {
        this.iSBP = iSBP;
    }

    public int getiDBP() {
        return iDBP;
    }

    public void setiDBP(int iDBP) {
        this.iDBP = iDBP;
    }

    public int getiHRate() {
        return iHRate;
    }

    public void setiHRate(int iHRate) {
        this.iHRate = iHRate;
    }

    public Integer [] getmPluse() {
        return mPluse;
    }

    public void setmPluse(Integer [] mPluse) {
        this.mPluse = mPluse;
    }

    public Integer [] getmEcg() {
        return mEcg;
    }

    public void setmEcg(Integer [] mEcg) {
        this.mEcg = mEcg;
    }

    public String getUserId() {
        return uid;
    }

    public void setUserId(String userId) {
        this.uid = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
