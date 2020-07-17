package com.vtech.app.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

/***
 * @author jason
 * 心率和 收缩压 舒张压 以及 平均值 最高值最低值 实体类
 * **/
public final class HealthBean implements Parcelable {
    //数据库
    private int id;
    private String creatTime;
    //用户id 备用
    private String userId;
    ///设备id
    private String deviceId;

    //原始数据
    private int isbp; //收缩压
    private int idbp; //舒张压
    private int ihrate;//心率
    //平均数据
    private int aIsbp;
    private int aIdbp;
    private int aIhrate;

    //最高值
    private int maxIsbp;
    private int maxIdbp;
    private int maxIhrate;

    //最低值
    private int minIsbp;
    private int minIdbp;
    private int minIhrate;

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

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

    public int getaIsbp() {
        return aIsbp;
    }

    public void setaIsbp(int aIsbp) {
        this.aIsbp = aIsbp;
    }

    public int getaIdbp() {
        return aIdbp;
    }

    public void setaIdbp(int aIdbp) {
        this.aIdbp = aIdbp;
    }

    public int getaIhrate() {
        return aIhrate;
    }

    public void setaIhrate(int aIhrate) {
        this.aIhrate = aIhrate;
    }

    public int getMaxIsbp() {
        return maxIsbp;
    }

    public void setMaxIsbp(int maxIsbp) {
        this.maxIsbp = maxIsbp;
    }

    public int getMaxIdbp() {
        return maxIdbp;
    }

    public void setMaxIdbp(int maxIdbp) {
        this.maxIdbp = maxIdbp;
    }

    public int getMaxIhrate() {
        return maxIhrate;
    }

    public void setMaxIhrate(int maxIhrate) {
        this.maxIhrate = maxIhrate;
    }

    public int getMinIsbp() {
        return minIsbp;
    }

    public void setMinIsbp(int minIsbp) {
        this.minIsbp = minIsbp;
    }

    public int getMinIdbp() {
        return minIdbp;
    }

    public void setMinIdbp(int minIdbp) {
        this.minIdbp = minIdbp;
    }

    public int getMinIhrate() {
        return minIhrate;
    }

    public void setMinIhrate(int minIhrate) {
        this.minIhrate = minIhrate;
    }

    public String getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "HealthBean{" +
                "id=" + id +
                ", creatTime='" + creatTime + '\'' +
                ", userId='" + userId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", isbp=" + isbp +
                ", idbp=" + idbp +
                ", ihrate=" + ihrate +
                ", aIsbp=" + aIsbp +
                ", aIdbp=" + aIdbp +
                ", aIhrate=" + aIhrate +
                ", maxIsbp=" + maxIsbp +
                ", maxIdbp=" + maxIdbp +
                ", maxIhrate=" + maxIhrate +
                ", minIsbp=" + minIsbp +
                ", minIdbp=" + minIdbp +
                ", minIhrate=" + minIhrate +
                '}';
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public HealthBean() {
    }

    public HealthBean(int isbp, int idbp, int ihrate, String creatTime) {
        this.creatTime = creatTime;
        this.isbp = isbp;
        this.idbp = idbp;
        this.ihrate = ihrate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.creatTime);
        dest.writeString(this.userId);
        dest.writeString(this.deviceId);
        dest.writeInt(this.isbp);
        dest.writeInt(this.idbp);
        dest.writeInt(this.ihrate);
        dest.writeInt(this.aIsbp);
        dest.writeInt(this.aIdbp);
        dest.writeInt(this.aIhrate);
        dest.writeInt(this.maxIsbp);
        dest.writeInt(this.maxIdbp);
        dest.writeInt(this.maxIhrate);
        dest.writeInt(this.minIsbp);
        dest.writeInt(this.minIdbp);
        dest.writeInt(this.minIhrate);
    }

    protected HealthBean(Parcel in) {
        this.id = in.readInt();
        this.creatTime = in.readString();
        this.userId = in.readString();
        this.deviceId = in.readString();
        this.isbp = in.readInt();
        this.idbp = in.readInt();
        this.ihrate = in.readInt();
        this.aIsbp = in.readInt();
        this.aIdbp = in.readInt();
        this.aIhrate = in.readInt();
        this.maxIsbp = in.readInt();
        this.maxIdbp = in.readInt();
        this.maxIhrate = in.readInt();
        this.minIsbp = in.readInt();
        this.minIdbp = in.readInt();
        this.minIhrate = in.readInt();
    }

    public static final Creator<HealthBean> CREATOR = new Creator<HealthBean>() {
        @Override
        public HealthBean createFromParcel(Parcel source) {
            return new HealthBean(source);
        }

        @Override
        public HealthBean[] newArray(int size) {
            return new HealthBean[size];
        }
    };
}
