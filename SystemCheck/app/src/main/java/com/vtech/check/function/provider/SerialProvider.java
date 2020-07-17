package com.vtech.check.function.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;


import com.vtech.check.function.bean.BeanUtil;
import com.vtech.check.function.main.pressure.BpressureTimeUtil;
import com.vtech.check.function.bean.HealthBean;
import com.vtech.check.function.bean.StatisticBean;
import com.vtech.check.function.provider.DbOpenHelper;
import com.vtech.check.function.utils.JsonUtil;

import java.util.ArrayList;


/****
 * @author jason
 * ContentProvider
 * */
public class SerialProvider extends ContentProvider {
    //   按日期，按周，按月 查询标志
    private static final String DAY = "1", WEEK = "2", MOUNTH = "3";

    //KEY 用于数据传输 的键
    private static final String KEY = "key";
    private static final String TAG = "SerialProvider";
    public static final String AUTHORITY = "com.vtech.check.healthprovider";
    //content provider 数据访问地址
    public static final Uri uri = Uri.parse("content://" + AUTHORITY);

    private static final UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    private SQLiteDatabase mDb;
    private Context mContext;
    private String mTable;
    //按日期分组
    private static final String GROUPBYDAY = " strftime('%Y-%m-%d'," + DbOpenHelper.HEALTH_CREATETIME + ")";
    //数据库字段 2017-01-01：11:22:12 ->   2017-01-01
    private static final String DAYTIME = " strftime('%Y-%m-%d'," + DbOpenHelper.HEALTH_CREATETIME + ") AS  " + DbOpenHelper.HEALTH_DAY;

    static {
        Log.d(TAG, "SerialProvider static start......");
        mUriMatcher.addURI(AUTHORITY, "", 0);
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {
        Log.d(TAG, "SerialProvider call method is:" + method + " arg =" + arg);
        Bundle bundle = new Bundle();
        if (TextUtils.isEmpty(arg)) {
            return bundle;
        }
        //血压 和心率
        switch (method) {
            case DAY: //按日期查询
                Log.d(TAG, "SerialProvider call start......date = ");
                //原始数据
                ArrayList<HealthBean> datas = getDayList(uri, arg, createSelectByDate());
                StatisticBean statisticDate = new StatisticBean();
                statisticDate.setList(datas);
                ///心率统计值
                HealthBean dateBean = getDayStatistical(uri, arg, createSelectByDate());
                if (dateBean != null) {
                    statisticDate.setMinRate(dateBean.getMinIhrate());
                    statisticDate.setMaxRate(dateBean.getMaxIhrate());
                    statisticDate.setAvgRate(dateBean.getaIhrate());
                    ///低压统计值
                    statisticDate.setMinDbp(dateBean.getMinIdbp());
                    statisticDate.setMaxDbp(dateBean.getMaxIdbp());
                    statisticDate.setAvgDbp(dateBean.getaIdbp());
                    ///高压统计值
                    statisticDate.setMinSbp(dateBean.getMinIsbp());
                    statisticDate.setMaxSbp(dateBean.getMaxIsbp());
                    statisticDate.setAvgSbp(dateBean.getaIsbp());
                }

                bundle.putString(KEY, JsonUtil.beanToJson(statisticDate));
                return bundle;
            case WEEK:
                Log.d(TAG, "SerialProvider call start......week = ");
                //原始数据
                ArrayList<HealthBean> weeksData = getWeekList(uri, createSelectByWeek(
                        BpressureTimeUtil.getWeekStart(arg), BpressureTimeUtil.getWeekEnd(arg)));
                StatisticBean statisticWeek = new StatisticBean();
                statisticWeek.setList(weeksData);
                ///心率统计值
                HealthBean weekBean = getWeekStatistical(uri, arg, createSelectByWeek(
                        BpressureTimeUtil.getWeekStart(arg), BpressureTimeUtil.getWeekEnd(arg)));
                if (weekBean != null) {
                    statisticWeek.setMinRate(weekBean.getMinIhrate());
                    statisticWeek.setMaxRate(weekBean.getMaxIhrate());
                    statisticWeek.setAvgRate(weekBean.getaIhrate());
                    ///低压统计值
                    statisticWeek.setMinDbp(weekBean.getMinIdbp());
                    statisticWeek.setMaxDbp(weekBean.getMaxIdbp());
                    statisticWeek.setAvgDbp(weekBean.getaIdbp());
                    ///高压统计值
                    statisticWeek.setMinSbp(weekBean.getMinIsbp());
                    statisticWeek.setMaxSbp(weekBean.getMaxIsbp());
                    statisticWeek.setAvgSbp(weekBean.getaIsbp());
                }

                bundle.putString(KEY, JsonUtil.beanToJson(statisticWeek));
                return bundle;
            case MOUNTH:
                Log.d(TAG, "SerialProvider call start......month = ");
                String mounth = arg.substring(0, arg.lastIndexOf("-"));
                //原始数据
                ArrayList<HealthBean> mounthData = getMounthList(uri, mounth, createSelectByMounth());
                StatisticBean statisticMounth = new StatisticBean();
                statisticMounth.setList(mounthData);
                ///心率统计值
                HealthBean mounthBean = getMounthStatistical(uri, mounth, createSelectByMounth());
                if (mounthBean != null) {
                    statisticMounth.setMinRate(mounthBean.getMinIhrate());
                    statisticMounth.setMaxRate(mounthBean.getMaxIhrate());
                    statisticMounth.setAvgRate(mounthBean.getaIhrate());
                    ///低压统计值
                    statisticMounth.setMinDbp(mounthBean.getMinIdbp());
                    statisticMounth.setMaxDbp(mounthBean.getMaxIdbp());
                    statisticMounth.setAvgDbp(mounthBean.getaIdbp());
                    ///高压统计值
                    statisticMounth.setMinSbp(mounthBean.getMinIsbp());
                    statisticMounth.setMaxSbp(mounthBean.getMaxIsbp());
                    statisticMounth.setAvgSbp(mounthBean.getaIsbp());
                }

                bundle.putString(KEY, JsonUtil.beanToJson(statisticMounth));
                return bundle;

        }

        return bundle;
    }


    @Override
    public int delete(Uri arg0, String arg1, String[] arg2) {
        return 0;
    }

    @Override
    public String getType(Uri arg0) {
        return null;
    }

    @Override
    public Uri insert(Uri arg0, ContentValues arg1) {
        Log.d(TAG, "SerialProvider insert");
        mDb.insert(mTable, null, arg1);
        mContext.getContentResolver().notifyChange(arg0, null);
        return null;
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "SerialProvider onCreate");
        mTable = DbOpenHelper.HEALTH_TABLE_NAME;
        mContext = getContext();
        initProvoder();
        return false;
    }

    private void initProvoder() {
        mDb = DbOpenHelper.getInstance(mContext).getWritableDatabase();
    }

    @Override
    public Cursor query(Uri arg0, String[] columns, String selection, String[] selectionArgs,
                        String groupBy) {
        Log.d(TAG, "SerialProvider query");
        String table = DbOpenHelper.HEALTH_TABLE_NAME;
        Cursor mCursor = mDb.query(table, columns, selection, selectionArgs, groupBy, null, null);
        return mCursor;
    }

    @Override
    public int update(Uri arg0, ContentValues arg1, String arg2, String[] arg3) {
        String table = DbOpenHelper.HEALTH_TABLE_NAME;
        int count = mDb.update(table, arg1, arg2, arg3);
        return count;
    }


    //获取指定日期的原始数据
    private ArrayList<HealthBean> getDayList(Uri uri, String date, String dateStr) {
        Cursor cursor = query(uri, new String[]{
                DbOpenHelper.HEALTH_ID, DbOpenHelper.HEALTH_IDBP,
                DbOpenHelper.HEALTH_ISBP, DbOpenHelper.HEALTH_IHRATE, DbOpenHelper.HEALTH_CREATETIME,
                DbOpenHelper.HEALTH_DEVICESID, DbOpenHelper.HEALTH_USERID,

        }, dateStr, new String[]{date}, null);

        if (cursor != null) {
            ArrayList<HealthBean> beans = BeanUtil.getHealths(cursor);
            Log.d(TAG, "bean：" + beans.size());
            for (HealthBean bean : beans) {
                Log.d(TAG, "bean==：" + bean.toString());
            }
            cursor.close();
            return beans;
        }
        return null;
    }

    //获取指定日期对应月份数据
    private ArrayList<HealthBean> getMounthList(Uri uri, String date, String dateStr) {
        Cursor cursor = query(uri, new String[]{
                DAYTIME,
                " AVG(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP,
                " AVG(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP,
                " AVG(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE,
                DbOpenHelper.HEALTH_ID, DbOpenHelper.HEALTH_CREATETIME,
                DbOpenHelper.HEALTH_DEVICESID, DbOpenHelper.HEALTH_USERID,

        }, dateStr, new String[]{date}, GROUPBYDAY);

        if (cursor != null) {
            ArrayList<HealthBean> beans = BeanUtil.getHealths(cursor);
            cursor.close();
            return beans;
        }
        return null;
    }


    //查询指定日期的统计数据
    private HealthBean getDayStatistical(Uri uri, String date, String dateStr) {
        Cursor cursor = query(uri, new String[]{
                " MAX(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_MAX,
                " AVG(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_AVG,
                " MIN(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_MIN,

                " MAX(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_MAX,
                " AVG(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_AVG,
                " MIN(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_MIN,

                " MAX(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_MAX,
                " AVG(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_AVG,
                " MIN(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_MIN

        }, dateStr, new String[]{date}, GROUPBYDAY);

        if (cursor != null && cursor.moveToFirst()) {
            HealthBean bean = BeanUtil.getHealth(cursor);
            cursor.close();
            return bean;
        }
        return null;
    }

    //查询指定日期对应月份的统计数据
    private HealthBean getMounthStatistical(Uri uri, String date, String dateStr) {
        Cursor cursor = query(uri, new String[]{
                DAYTIME,
                " MAX(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_MAX,
                " AVG(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_AVG,
                " MIN(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_MIN,

                " MAX(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_MAX,
                " AVG(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_AVG,
                " MIN(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_MIN,

                " MAX(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_MAX,
                " AVG(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_AVG,
                " MIN(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_MIN

        }, dateStr, new String[]{date}, null);

        if (cursor != null && cursor.moveToFirst()) {
            HealthBean bean = BeanUtil.getHealth(cursor);
            cursor.close();
            return bean;
        }
        return null;
    }

    //查询指定日期对应周的数据
    private ArrayList<HealthBean> getWeekList(Uri uri, String dateStr) {
        Cursor cursor = query(uri, new String[]{
                DAYTIME,
                " AVG(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP,
                " AVG(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP,
                " AVG(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE,
                DbOpenHelper.HEALTH_ID,
                DbOpenHelper.HEALTH_CREATETIME,
                DbOpenHelper.HEALTH_DEVICESID, DbOpenHelper.HEALTH_USERID,

        }, dateStr, null, GROUPBYDAY);

        if (cursor != null) {
            ArrayList<HealthBean> beans = BeanUtil.getHealths(cursor);
            cursor.close();
            return beans;
        }
        return null;
    }

    //查询指定日期对应周的统计数据
    private HealthBean getWeekStatistical(Uri uri, String dateStart, String dateStr) {

        Log.d(TAG, dateStart + "  week 第一天 " + BpressureTimeUtil.getWeekStart(dateStart));
        Log.d(TAG, dateStart + "  week 最后一天 " + BpressureTimeUtil.getWeekEnd(dateStart));

        Cursor cursor = query(uri, new String[]{
                DAYTIME,
                " MAX(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_MAX,
                " AVG(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_AVG,
                " MIN(" + DbOpenHelper.HEALTH_IHRATE + ") AS " + DbOpenHelper.HEALTH_IHRATE_MIN,

                " MAX(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_MAX,
                " AVG(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_AVG,
                " MIN(" + DbOpenHelper.HEALTH_ISBP + ") AS " + DbOpenHelper.HEALTH_ISBP_MIN,

                " MAX(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_MAX,
                " AVG(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_AVG,
                " MIN(" + DbOpenHelper.HEALTH_IDBP + ") AS " + DbOpenHelper.HEALTH_IDBP_MIN
        }, dateStr, null, GROUPBYDAY);

        if (cursor != null && cursor.moveToFirst()) {
            HealthBean bean = BeanUtil.getHealth(cursor);
            cursor.close();
            return bean;
        }
        return null;
    }

    //按指定年月查询
    public String createSelectByMounth() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("strftime('%Y-%m',");
        stringBuffer.append(DbOpenHelper.HEALTH_CREATETIME);
        stringBuffer.append(")=?");
        return stringBuffer.toString();
    }

    //按指定周查询
    public String createSelectByWeek(String startDate, String endDate) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("datetime(");
        stringBuffer.append(DbOpenHelper.HEALTH_CREATETIME + ")");
        stringBuffer.append(" between ");
        stringBuffer.append("datetime('" + startDate + "')");
        stringBuffer.append(" and ");
        stringBuffer.append(" datetime('" + endDate + "')");
        return stringBuffer.toString();

    }

    //按天查询
    public String createSelectByDate() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("strftime('%Y-%m-%d',");
        stringBuffer.append(DbOpenHelper.HEALTH_CREATETIME);
        stringBuffer.append(")=?");
        return stringBuffer.toString();
    }

}
