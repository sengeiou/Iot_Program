package com.vtech.vhealth.function.bean;

import java.util.ArrayList;

/***
 * @author jason
 * 心率和 收缩压 舒张压 原始数据集合 以及
 * 统计数据 以及 平均值 最高值最低值
 * 实体类
 * */
public class StatisticBean {
    private ArrayList<HealthBean> list;
    //心率
    private int maxRate;
    private int minRate;
    private int avgRate;
    // 舒张压
    private int maxDbp;
    private int minDbp;
    private int avgDbp;
    ///收缩压
    private int maxSbp;
    private int minSbp;
    private int avgSbp;


    public ArrayList<HealthBean> getList() {
        return list;
    }

    public void setList(ArrayList<HealthBean> list) {
        this.list = list;
    }

    public int getMaxRate() {
        return maxRate;
    }

    public void setMaxRate(int maxRate) {
        this.maxRate = maxRate;
    }

    public int getMinRate() {
        return minRate;
    }

    public void setMinRate(int minRate) {
        this.minRate = minRate;
    }

    public int getAvgRate() {
        return avgRate;
    }

    public void setAvgRate(int avgRate) {
        this.avgRate = avgRate;
    }

    public int getMaxDbp() {
        return maxDbp;
    }

    public void setMaxDbp(int maxDbp) {
        this.maxDbp = maxDbp;
    }

    public int getMinDbp() {
        return minDbp;
    }

    public void setMinDbp(int minDbp) {
        this.minDbp = minDbp;
    }

    public int getAvgDbp() {
        return avgDbp;
    }

    public void setAvgDbp(int avgDbp) {
        this.avgDbp = avgDbp;
    }

    public int getMaxSbp() {
        return maxSbp;
    }

    public void setMaxSbp(int maxSbp) {
        this.maxSbp = maxSbp;
    }

    public int getMinSbp() {
        return minSbp;
    }

    public void setMinSbp(int minSbp) {
        this.minSbp = minSbp;
    }

    public int getAvgSbp() {
        return avgSbp;
    }

    public void setAvgSbp(int avgSbp) {
        this.avgSbp = avgSbp;
    }


    @Override
    public String toString() {
        return "StatisticBean{" +
                "list=" + list +
                ", maxRate=" + maxRate +
                ", minRate=" + minRate +
                ", avgRate=" + avgRate +
                ", maxDbp=" + maxDbp +
                ", minDbp=" + minDbp +
                ", avgDbp=" + avgDbp +
                ", maxSbp=" + maxSbp +
                ", minSbp=" + minSbp +
                ", avgSbp=" + avgSbp +
                '}';
    }
}
