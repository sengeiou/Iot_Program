package com.vtech.vhealth.function.main.pressure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式转换工具
 **/
public class BpressureTimeUtil {

    private static final String DATE_FORMATTER = "yyyy-MM-dd HH:mm:ss";
    private static final String DATE_FORMATTER_WEEK = "yyyy-MM-dd";


    /**
     * 获取当前时间 类型的时间格式
     * 时间格式为：yyyy/MM/dd HH:mm:ss
     */
    public static String getCurTime() {
        SimpleDateFormat ftf = new  SimpleDateFormat(DATE_FORMATTER, Locale.getDefault());
        return ftf.format(new Date());
    }

    /**
     * 获取当前时间 类型的时间格式
     * 时间格式为：yyyy/MM/dd HH:mm:ss
     */
    public static String getCurDay() {
        SimpleDateFormat ftf = new  SimpleDateFormat(DATE_FORMATTER_WEEK, Locale.getDefault());
        return ftf.format(new Date());
    }
    /**
     * 获取本周的第一天 一周第一天 是星期天
     *
     * @return String
     **/
    public static String getWeekStart(String dateStr) {
        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER_WEEK, Locale.getDefault());
            Date date = sdf.parse(dateStr);
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, cal.getFirstDayOfWeek());
            Date time = cal.getTime();
            String timeStart = sdf.format(time);
            return timeStart;

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 获取本周的最后一天
     *
     * @return String
     **/
    public static String getWeekEnd(String dateStr) {

        try {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMATTER_WEEK, Locale.getDefault());
            Date date = sdf.parse(dateStr);
            cal.setTime(date);
            cal.set(Calendar.DAY_OF_WEEK, cal.getActualMaximum(Calendar.DAY_OF_WEEK));
            cal.add(Calendar.DAY_OF_WEEK, 0);
            Date time = cal.getTime();
            String timeStart = sdf.format(time);
            return timeStart;

        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }

    }

    public static void main(String[] args) {
        String d = "2019-09-16";
        System.out.println("sss="+d.substring(0,d.lastIndexOf("-")));
        System.out.println("" + getWeekStart(d));
        System.out.println("" + getWeekEnd(d));
    }


}
