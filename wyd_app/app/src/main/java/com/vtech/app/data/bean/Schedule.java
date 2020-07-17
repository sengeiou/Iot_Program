package com.vtech.app.data.bean;

public  class Schedule {
    public String title;
    public String remark;
    public long startTime;
    public long endTime;
    public String repeatDay;

    public Schedule(String title, String remark, long startTime, long endTime, String repeatDay) {
        this.title = title;
        this.remark = remark;
        this.startTime = startTime;
        this.endTime = endTime;
        this.repeatDay = repeatDay;
    }
}