package com.vtech.vhealth.function.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间格式转换工具
 **/
public class TimeUtil {

    private static final String DATE_FORMATTER0 = "HH:mm";
    private static final String DATE_FORMATTER = "yyyy/MM/dd HH:mm:ss";
    private static final String DAY_FORMATTER = "yyyy/MM/dd";
    private static final String DAY_FORMATTER1 = "yyyy-MM-dd";

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
    public static String getTime() {
        SimpleDateFormat ftf = new  SimpleDateFormat(DATE_FORMATTER0, Locale.getDefault());
        return ftf.format(new Date());
    }


    /**
     * 获取当前时间 类型的时间格式
     * 时间格式为：yyyy/MM/dd HH:mm:ss
     */
    public static String getCurDay() {
        SimpleDateFormat ftf = new  SimpleDateFormat(DAY_FORMATTER, Locale.getDefault());
        return ftf.format(new Date());
    }


    /**
     * 获取当前时间 类型的时间格式
     * 时间格式为：yyyy/MM/dd HH:mm:ss
     */
    public static String getDay() {
        SimpleDateFormat ftf = new  SimpleDateFormat(DAY_FORMATTER1, Locale.getDefault());
        return ftf.format(new Date());
    }

    public static  String getAmPm(){
        long time = System.currentTimeMillis();
        final Calendar mCalendar = Calendar.getInstance();
        mCalendar.setTimeInMillis(time);

        int hour = mCalendar.get(Calendar.HOUR);

        int apm = mCalendar.get(Calendar.AM_PM);
        if (apm == 0) {
            return "AM";
        }else {
            return "PM";
        }

//        apm=0 表示上午，apm=1表示下午。
    }

    public static void main(String[] args) {
        String d = "2019-09-16";
//        convertTimeToStr();
//        System.out.println("sss="+d.substring(0,d.lastIndexOf("-")));
//        System.out.println("" + getWeekStart(d));
//        System.out.println("" + getWeekEnd(d));
//        System.out.println("" + getCurTime());
        byte[] bytes=new byte[10];
        bytes[0]=-1;
        bytes[1]=2;
        int high = bytes[0] & 0xff;
        int low = bytes[1] & 0xff;
        System.out.println(" 急急急"+((bytes[0] << 8 | bytes[1])));
        System.out.println(" 急急急bytes[0] << 8 ="+(bytes[0] << 8 )+ " 结果："+((bytes[0] << 8 | bytes[1])));

        System.out.println(" h=*255= "+( high*256));

        System.out.println(" h="+high+" high*255 "+(high*256 )+" l="+low +" 合成："+( high*256+low));

        String time= getTime();
        System.out.printf(" ======"+time);

    }

}
