package com.vtech.app.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;
import com.vtech.app.data.bean.HomeProtectZoneStatusBean;
import com.vtech.app.data.bean.HomeSecurityCombineZoneStatusBean;
import com.vtech.app.data.bean.HomeSecurityEventBean;

import java.util.ArrayList;

public class HomeSecurityUtil {
    public static final String TAG = "HomeSecurityUtil";

    private static final String AUTHORITY = "com.vtech.homesecurity.provider";
    private static final String HP_EVENT_TABLE = "safetyhistorybean";
    private static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + HP_EVENT_TABLE);
    private static final String HP_ZONE_STATUS_TABLE = "protectedzonebean";
    private static final Uri HP_ZONE_STATUS_URI = Uri.parse("content://" + AUTHORITY + "/" + HP_ZONE_STATUS_TABLE);

    private static final String HP_COMBINE_ZONE_STATUS_TABLE = "combinedeployzonebean";
    private static final Uri HP_COMBINE_ZONE_STATUS_URI = Uri.parse("content://" + AUTHORITY + "/" + HP_COMBINE_ZONE_STATUS_TABLE);


    public static ArrayList<HomeSecurityEventBean> getAllHPEvent(Context context) {
        ArrayList<HomeSecurityEventBean> list = new ArrayList<>();
        try {
            Cursor cursor = context.getContentResolver().query(URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String eventName = cursor.getString(cursor.getColumnIndex("eventname"));
                    String probeName = cursor.getString(cursor.getColumnIndex("probename"));
                    String place = cursor.getString(cursor.getColumnIndex("place"));
                    String dateTime = cursor.getString(cursor.getColumnIndex("datetime"));
                    String protectZoneName = cursor.getString(cursor.getColumnIndex("protectzonename"));
                    int probeType = cursor.getInt(cursor.getColumnIndex("probetype"));
                    HomeSecurityEventBean eventBean = new HomeSecurityEventBean(eventName, probeName, place, dateTime, protectZoneName, probeType);
                    list.add(eventBean);
                }
                cursor.close();
            }
            Logger.i(TAG,"getAllHPEvent  list : "+new Gson().toJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    public static int getHPEventTimesByEventType(Context context, int type) {
        ArrayList<HomeSecurityEventBean> list = new ArrayList<>();
        int times = 0;
        StringBuilder sb = new StringBuilder();
        sb.append("probetype = ?");
        String select = sb.toString();
        String[] selectArgs = new String[]{type + ""};
        Cursor cursor = context.getContentResolver().query(URI, null, select, selectArgs, null);
        if (cursor != null) {
            times = cursor.getCount();
            cursor.close();
        }
        return times;
    }


    public static ArrayList<HomeProtectZoneStatusBean> getAllHPZoneStatus(Context context) {
        ArrayList<HomeProtectZoneStatusBean> list = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        sb.append("isprotected = ?");
        String select = sb.toString();
        String[] selectArgs = new String[]{1 + ""};
        try {
            Cursor cursor = context.getContentResolver().query(HP_ZONE_STATUS_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("name"));
                    String probe = cursor.getString(cursor.getColumnIndex("probe"));
                    String zone = cursor.getString(cursor.getColumnIndex("zone"));
                    String place = cursor.getString(cursor.getColumnIndex("place"));
                    boolean isNormal = cursor.getInt(cursor.getColumnIndex("isNormal")) > 0 ? true : false;
                    HomeProtectZoneStatusBean statusBean = new HomeProtectZoneStatusBean(name, probe, zone, place, isNormal);
                    list.add(statusBean);
                }
                cursor.close();
            }
            ///////
            Logger.i(TAG,"getAllHPZoneStatus  list : "+new Gson().toJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    //联动布防布防时间选项
    public static final int TRIGGER_TIME_SELECT_DURATION = 2;

    public static ArrayList<HomeSecurityCombineZoneStatusBean> getAllCombineZoneDatas(Context context) {
        ArrayList<HomeSecurityCombineZoneStatusBean> list = new ArrayList<>();
        try {
            Cursor cursor = context.getContentResolver().query(HP_COMBINE_ZONE_STATUS_URI, null, null, null, null);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("zoneName"));
                    int timeSelect = cursor.getInt(cursor.getColumnIndex("timeSelect"));
                    int timeDuration = cursor.getInt(cursor.getColumnIndex("timeDuration"));
                    long startTime = cursor.getLong(cursor.getColumnIndex("startTime"));
                    long endTime = cursor.getLong(cursor.getColumnIndex("endTime"));
                    int triggerStatus = cursor.getInt(cursor.getColumnIndex("triggerStatus"));
                    long triggerTime = cursor.getLong(cursor.getColumnIndex("triggerTime"));
                    if (timeSelect == TRIGGER_TIME_SELECT_DURATION) {
                        startTime = triggerTime;
                        endTime = triggerTime + (timeDuration * 60 * 1000);
                    }

                    int dialResultSuccess = cursor.getInt(cursor.getColumnIndex("dialResultSuccess"));
                    int smsResultSuccess = cursor.getInt(cursor.getColumnIndex("smsResultSuccess"));

                    HomeSecurityCombineZoneStatusBean statusBean = new HomeSecurityCombineZoneStatusBean(name, startTime, endTime, triggerStatus, dialResultSuccess, smsResultSuccess);
                    list.add(statusBean);
                }
                cursor.close();
            }
            ///////
            Logger.i(TAG,"getAllCombineZoneDatas  list : "+new Gson().toJson(list));
        }catch (Exception e){
            e.printStackTrace();
        }

        return list;
    }

}
