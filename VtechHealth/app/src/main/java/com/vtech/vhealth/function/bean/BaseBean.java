package com.vtech.vhealth.function.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseBean implements Parcelable {


    /**
     * code : 0
     * msg : 更新用户头像成功
     */

    protected String code;
    protected String msg;

    public static final Creator<BaseBean> CREATOR = new Creator<BaseBean>() {
        @Override
        public BaseBean createFromParcel(Parcel in) {
            return new BaseBean(in);
        }

        @Override
        public BaseBean[] newArray(int size) {
            return new BaseBean[size];
        }
    };

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }



    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.code);
        dest.writeString(this.msg);
    }

    public BaseBean() {
    }

    protected BaseBean(Parcel in) {
        this.code = in.readString();
        this.msg = in.readString();
    }

    @Override
    public String toString() {
        return "IconBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                '}';
    }
}
