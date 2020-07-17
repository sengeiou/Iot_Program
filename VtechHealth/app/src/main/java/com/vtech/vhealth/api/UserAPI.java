package com.vtech.vhealth.api;


import android.util.Log;

import com.vtech.vhealth.api.okhttp.BaseParams;
import com.vtech.vhealth.api.okhttp.BodyCallback;
import com.vtech.vhealth.function.bean.UserBean;
import com.zhy.http.okhttp.OkHttpUtils;

import java.io.File;

/**
 * 用户网络数据模块请求
 */
public class UserAPI extends HttpUrl {
    public static final String TAG = "UserAPI";

    /**
     * 上传健康数据 心率 血压
     */
    public static void addHealths(String heartRates, BodyCallback callback) {
        BaseParams.getPostBuilder()
                .addParams("heartRates", heartRates)
                .url(PUSH_HEALTH)
                .build()
                .execute(callback);
    }

    /**
     * 上传心电数据
     */
    public static void addEcgData(String deviceId, String ecg,
                                  String nn50sdnn, String hflf, String pnn50,
                                  String afib, String heartage, String hrv,
                                  String rhr, String mood, String stress,
                                  BodyCallback callback) {
        Log.i(TAG, "deviceId : " + deviceId + " ,ecg : " + ecg +
                " ,nn50sdnn : " + nn50sdnn + " ,hflf : " + hflf + " ,pnn50 : " + pnn50 + " ,afib : " + afib + " ,heartage : " + heartage);
        BaseParams.getPostBuilder()
                .addParams("deviceId", deviceId)
                .addParams("time", String.valueOf(System.currentTimeMillis()))
                .addParams("ecg", ecg)
                .addParams("nn50sdnn", nn50sdnn)
                .addParams("hflf", hflf)
                .addParams("pnn50", pnn50)
                .addParams("afib", afib)
                .addParams("heartage", heartage)
                .addParams("hrv", hrv)
                .addParams("rhr", rhr)
                .addParams("mood", mood)
                .addParams("stress", stress)
                .url(PUSH_ECG_DATA)
                .build()
                .execute(callback);
    }


    /**
     * 更新用户资料
     * device_id  设备Id
     * device_user_id  设备用户 ID
     * name  设备用户名
     * age  设备用户年龄
     * sex  设备用户性别
     * address  设备用户地址
     * identity 设备用户身份
     * image 用户头像
     */
    public static void updateUserInfo(String device_id, String device_user_id,UserBean userBean , BodyCallback callback) {
        BaseParams.getPostBuilder()
                .addParams("device_id", device_id)
                .addParams("device_user_id", device_user_id)
                .addParams("name", userBean.getUserName())
                .addParams("age", ""+userBean.getAge())
                .addParams("sex", userBean.getSex()) // 0女 1 男
                .addParams("address", userBean.getArea())
                .addParams("identity", userBean.getUid())
                .url(PUSH_DEVICESER)
                .build()
                .execute(callback);
    }

    /**
     * 更新用户头像
     * device_id
     * device_user_id
     * image
     */
    public static void updateUserIcon(String device_id, String device_user_id, File image, BodyCallback callback) {
        BaseParams.getPostBuilder()
                .addParams("device_id", device_id)
                .addParams("device_user_id", device_user_id)
                .addFile("image", image.getName(),image)
                .url(PUSH_USERICON)
                .build()
                .execute(callback);
    }

    /**
     * 获取用户信息
     * device_id
     * device_user_id
     * image
     */
    public static void getUserInfo(String device_id, String device_user_id, BodyCallback callback) {
        OkHttpUtils.get()
                .url(BASEURL+"deviceuser/"+device_user_id+"?device_id="+device_id)
                .build()
                .execute(callback);
    }

}