package com.vtech.app.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;
import com.vtech.app.data.bean.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ScheduleUtil {
    public  static final String TAG = "ScheduleUtil";
    private static final String AUTHORITY = "com.vtech.schedule.provider";
    private static final String SCHEDULE_TABLE = "schedule";
    private static final Uri URI = Uri.parse("content://" + AUTHORITY + "/" + SCHEDULE_TABLE);

    public static ArrayList<Schedule> getTodaySchedule(Context context) {
        ArrayList<Schedule> list = new ArrayList<>();
        String curMinute = String.valueOf(removeSecond(System.currentTimeMillis()));
        String todayEndTime = String.valueOf(getDayEndTime(System.currentTimeMillis()));
        ///////
        StringBuilder sb = new StringBuilder();
        sb.append("starttime > ? and starttime < ? and endtime = ?"); //闹钟
        sb.append(" or ");
        sb.append("endTime != ? and endTime > ?");
        String select = sb.toString();
        String[] selectArgs = new String[]{curMinute, todayEndTime, "0", "0", curMinute};
        String sort = "startTime";
        try {
            Cursor cursor = context.getContentResolver().query(URI, null, select, selectArgs, sort);
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String title = cursor.getString(cursor.getColumnIndex("title"));
                    String remark = cursor.getString(cursor.getColumnIndex("remark"));
                    long startTime = cursor.getLong(cursor.getColumnIndex("starttime"));
                    long endTime = cursor.getLong(cursor.getColumnIndex("endtime"));
                    String repeatDay = cursor.getString(cursor.getColumnIndex("repeatday"));
                    Schedule schedule = new Schedule(title, remark, startTime, endTime, repeatDay);
                    list.add(schedule);
                }
                cursor.close();
            }
            Logger.i(TAG, "getTodaySchedule list : " + new Gson().toJson(list));
            ///////
            return list;
        }catch (Exception e){
            e.printStackTrace();
        }
        return list;
    }

    private static long removeSecond(long time) {
        long minute = 1000 * 60;
        time = (time / minute) * minute;
        return time;
    }

    private static long getDayEndTime(long time) {
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(time);
        c.set(Calendar.HOUR_OF_DAY, 23);
        c.set(Calendar.MINUTE, 59);
        c.set(Calendar.SECOND, 59);
        Date m6 = c.getTime();
        return m6.getTime();
    }
}
