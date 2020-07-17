package com.vtech.check.function.bean;

import android.database.Cursor;
import android.util.Log;


import com.vtech.check.function.provider.DbOpenHelper;

import java.util.ArrayList;

public class BeanUtil {


    private static final String TAG = "BeanUtil";

    /**
     * 从Cursor 生成  healthBean  对象
     * @param cursor
     * @return
     */
    public static HealthBean getHealth(Cursor cursor) {

        HealthBean healthBean = new HealthBean();

        int id = cursor.getColumnIndex(DbOpenHelper.HEALTH_ID);
        int sbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_ISBP);
        int dbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_IDBP);
        int rate = cursor.getColumnIndex(DbOpenHelper.HEALTH_IHRATE);
        int time = cursor.getColumnIndex(DbOpenHelper.HEALTH_CREATETIME);
        int device = cursor.getColumnIndex(DbOpenHelper.HEALTH_DEVICESID);
        int user = cursor.getColumnIndex(DbOpenHelper.HEALTH_USERID);

        int maxRate = cursor.getColumnIndex(DbOpenHelper.HEALTH_IHRATE_MAX);
        int minRate = cursor.getColumnIndex(DbOpenHelper.HEALTH_IHRATE_MIN);
        int avgRate = cursor.getColumnIndex(DbOpenHelper.HEALTH_IHRATE_AVG);

        int maxDbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_IDBP_MAX);
        int minDbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_IDBP_MIN);
        int avgDbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_IDBP_AVG);

        int maxSbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_ISBP_MAX);
        int minSbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_ISBP_MIN);
        int avgSbp = cursor.getColumnIndex(DbOpenHelper.HEALTH_ISBP_AVG);

        int userId = cursor.getColumnIndex(DbOpenHelper.HEALTH_USERID);
        int devicesId = cursor.getColumnIndex(DbOpenHelper.HEALTH_DEVICESID);



        //数据库ID
        if (id != -1) {
            healthBean.setId(cursor.getInt(id));
        }
        //设备ID
        if (devicesId != -1) {
            healthBean.setDeviceId(cursor.getString(devicesId));
        }

        //用户ID
        if (userId != -1) {
            healthBean.setUserId(cursor.getString(userId));
        }


        if (sbp != -1) {
            healthBean.setIsbp(cursor.getInt(sbp));
        }
        if (dbp != -1) {
            healthBean.setIdbp(cursor.getInt(dbp));
        }
        if (rate != -1) {
            healthBean.setIhrate(cursor.getInt(rate));
        }
        if (time != -1) {
            healthBean.setCreatTime(cursor.getString(time));
        }
        if (device != -1) {
            healthBean.setDeviceId(cursor.getString(device));
        }
        if (user != -1) {
            healthBean.setUserId(cursor.getString(user));
        }

        //心率
        if (maxRate != -1) {
            healthBean.setMaxIhrate(cursor.getInt(maxRate));
        }
        if (minRate != -1) {
            healthBean.setMinIhrate(cursor.getInt(minRate));
        }
        if (avgRate != -1) {
            healthBean.setaIhrate(cursor.getInt(avgRate));
        }
        //收缩压 高压
        if (maxSbp != -1) {
            healthBean.setMaxIsbp(cursor.getInt(maxSbp));
        }
        if (minSbp != -1) {
            healthBean.setMinIsbp(cursor.getInt(minSbp));
        }
        if (avgSbp != -1) {
            healthBean.setaIsbp(cursor.getInt(avgSbp));
        }

//        舒张压   低压
        if (maxDbp != -1) {
            healthBean.setMaxIdbp(cursor.getInt(maxDbp));
        }
        if (minDbp != -1) {
            healthBean.setMinIdbp(cursor.getInt(minDbp));
        }
        if (avgDbp != -1) {
            healthBean.setaIdbp(cursor.getInt(avgDbp));
        }

        Log.d(TAG, " ： healthBean " + healthBean.toString());

        return healthBean;
    }

    public static ArrayList<HealthBean> getHealths(Cursor cursor) {
        ArrayList<HealthBean> beanList = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                HealthBean bean = BeanUtil.getHealth(cursor);
                beanList.add(bean);
                Log.d(TAG, "bean：" + bean.toString());
            } while (cursor.moveToNext());

        }
        return beanList;
    }

}