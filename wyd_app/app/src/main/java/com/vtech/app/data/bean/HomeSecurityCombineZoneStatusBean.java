package com.vtech.app.data.bean;

public final class HomeSecurityCombineZoneStatusBean {

    private String zoneName;

    private long startTime;
    
    private long endTime;

    //触发状态，0--未触发; 1--已触发一个，等待第二个（如门磁已触发，等待红外人体信号）;2--此联动防区已触发; 3--此联动防区在布防时间内，但已经得到正常状态了，不会再监测; 4--异常
    private int triggerStatus;
    
    //打电话的结果，0---未有任何通话被接通， 1--有通话被接通
    private int dialResultSuccess;

    //发短信的结果，0---未有短信发送出去， 1--有信息发送成功
    private int smsResultSuccess;

    public HomeSecurityCombineZoneStatusBean(String zoneName, long startTime, long endTime, int triggerStatus, int dialResultSuccess, int smsResultSuccess) {
        this.zoneName = zoneName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.triggerStatus = triggerStatus;
        this.dialResultSuccess = dialResultSuccess;
        this.smsResultSuccess = smsResultSuccess;
    }

    public String getZoneName() {
        return zoneName;
    }

    public void setZoneName(String zoneName) {
        this.zoneName = zoneName;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public int getTriggerStatus() {
        return triggerStatus;
    }

    public void setTriggerStatus(int triggerStatus) {
        this.triggerStatus = triggerStatus;
    }

    public int getDialResultSuccess() {
        return dialResultSuccess;
    }

    public void setDialResultSuccess(int dialResultSuccess) {
        this.dialResultSuccess = dialResultSuccess;
    }

    public int getSmsResultSuccess() {
        return smsResultSuccess;
    }

    public void setSmsResultSuccess(int smsResultSuccess) {
        this.smsResultSuccess = smsResultSuccess;
    }
}
