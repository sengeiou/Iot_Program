package com.vtech.app.util;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vtech.app.data.bean.StatisticBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HealthUtils {
    private static final String TAG = "HealthUtils";
    //按日期，按周，按月 查询标志
    public static final String AUTHORITY = "com.vtech.vhealth.healthprovider";


    public static final int DAY = 1, WEEK = 2, MOUNTH = 3;

    static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    public static List getHealthData(Context context) {
        Uri uri = Uri.parse("content://" + AUTHORITY);
        Bundle bundle = context.getContentResolver().call(uri, String.valueOf(DAY), format.format(new Date()), null);

        String key = bundle.getString("key");

        Logger.i(TAG, "key :" + key);
        return null;
    }


    public static StatisticBean getHealthData(Context context, int type) {
        StatisticBean bean = new StatisticBean();
        Uri uri = Uri.parse("content://" + AUTHORITY);
        try {
            Bundle bundle = context.getContentResolver().call(uri, String.valueOf(type), format.format(new Date()), null);
            String key = bundle.getString("key");
            bean = new Gson().fromJson(key, StatisticBean.class);
            Logger.i(TAG, "type : " + type + "  data size is : " + bean.getList().size() + " ,key :" + key);

        }catch (Exception e){
            e.printStackTrace();
        }
        return bean;
    }

    public static final String ECG_AUTHORITY = "com.vtech.vhealth.ecgprovider";


    private static Uri uri =Uri.parse("content://"+ ECG_AUTHORITY);
    public static  String KEY_HEALTH_ECG = "health_ecg";
    public static List<Integer>  getEcgData(Context context){
        List<Integer> list = new ArrayList<>();
        try{
            Bundle bundle = context.getContentResolver().call(uri, "ecg", "", null);
            if (bundle != null) {
                String key=bundle.getString(KEY_HEALTH_ECG);
                Log.i(TAG,"getEcgData ecgkey ---------------------- "+key);
                list = new Gson().fromJson(key, new TypeToken<List<Integer>>() {}.getType());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

}
