package com.vtech.app.data.bean;

public final class HomeSecurityEventBean {
    private String eventName;
    private String probeName;
    private String place;
    private String dateTime;
    private String protectZoneName;
    private int probeType;

    public HomeSecurityEventBean(String eventName, String probeName, String place, String dateTime, String protectZoneName, int probeType) {
        this.eventName = eventName;
        this.probeName = probeName;
        this.place = place;
        this.dateTime = dateTime;
        this.protectZoneName = protectZoneName;
        this.probeType = probeType;
    }


    public String toString() {
        return this.eventName + ", " + this.probeName + ", " + this.place + ", " + this.dateTime + ", " + this.protectZoneName + ", " + this.probeType;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getProbeName() {
        return probeName;
    }

    public void setProbeName(String probeName) {
        this.probeName = probeName;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getProtectZoneName() {
        return protectZoneName;
    }

    public void setProtectZoneName(String protectZoneName) {
        this.protectZoneName = protectZoneName;
    }

    public int getProbeType() {
        return probeType;
    }

    public void setProbeType(int probeType) {
        this.probeType = probeType;
    }
}
