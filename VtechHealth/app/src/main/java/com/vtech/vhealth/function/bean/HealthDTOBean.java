package com.vtech.vhealth.function.bean;

import java.util.LinkedList;
import java.util.List;

public class HealthDTOBean {

    private String time;    //上传时间
    private String uid; //用户id
    private String deviceId; //设备id

    private int iSBP;
    private int iDBP;
    private int iHRate;

    private int aISBP;
    private int aIDBP;
    private int aIHRate;

    private int calibrationStatus;
    private int clearCalibration;

    //脉搏
    private List<Integer> mPluse = new LinkedList<>();
    //心电
    private List<Integer> mEcg = new LinkedList<>();

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

    public List<Integer> getmPluse() {
        return mPluse;
    }

    public void setmPluse(List<Integer> mPluse) {
        this.mPluse = mPluse;
    }

    public List<Integer> getmEcg() {
        return mEcg;
    }

    public void setmEcg(List<Integer> mEcg) {
        this.mEcg = mEcg;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public int getaISBP() {
        return aISBP;
    }

    public void setaISBP(int aISBP) {
        this.aISBP = aISBP;
    }

    public int getaIDBP() {
        return aIDBP;
    }

    public void setaIDBP(int aIDBP) {
        this.aIDBP = aIDBP;
    }

    public int getaIHRate() {
        return aIHRate;
    }

    public void setaIHRate(int aIHRate) {
        this.aIHRate = aIHRate;
    }

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

    @Override
    public String toString() {
        return "HealthDTOBean{" +
                "time='" + time + '\'' +
                ", uid='" + uid + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", iSBP=" + iSBP +
                ", iDBP=" + iDBP +
                ", iHRate=" + iHRate +
                ", aISBP=" + aISBP +
                ", aIDBP=" + aIDBP +
                ", aIHRate=" + aIHRate +
                ", calibrationStatus=" + calibrationStatus +
                ", clearCalibration=" + clearCalibration +
                ", mPluse=" + mPluse +
                ", mEcg=" + mEcg +
                '}';
    }
}
