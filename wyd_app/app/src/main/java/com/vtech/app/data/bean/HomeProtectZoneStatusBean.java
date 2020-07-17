package com.vtech.app.data.bean;

public final class HomeProtectZoneStatusBean {

    private String name;

    //探测器， 如红外、燃气……
    private String probe;

    //安装位置，如大门、客厅……
    private String zone;

    //安装区域，如客厅窗户1、客厅窗户2……
    private String place;

    //探测器状态： 异常（0）; 正常（yuyin1）
    private boolean isNormal;


    public HomeProtectZoneStatusBean(String name, String probe, String zone, String place, boolean isNormal) {
        this.name = name;
        this.probe = probe;
        this.zone = zone;
        this.place = place;
        this.isNormal = isNormal;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProbe() {
        return probe;
    }

    public void setProbe(String probe) {
        this.probe = probe;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public boolean isNormal() {
        return isNormal;
    }

    public void setNormal(boolean normal) {
        isNormal = normal;
    }
}
