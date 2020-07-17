package com.vtech.check.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vtech.check.R;

import java.util.Calendar;
import java.util.TimeZone;

public class ClockTest extends  BaseTest{

    View rootview;
    private static final int msgKey1 = 1;
    private TextView mTime;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            rootview=inflater.inflate(R.layout.clock,container,false);
        initView();
        return rootview;
    }

    private void initView() {
        mTime = (TextView) rootview.findViewById(R.id.mytime);
        new TimeThread().start();
    }



    public class TimeThread extends Thread {
        @Override
        public void run () {
            do {
                try {
                    Thread.sleep(1000);
                    Message msg = new Message();
                    msg.what = msgKey1;
                    mHandler.sendMessage(msg);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while(true);
        }
    }
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage (Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case msgKey1:
                    mTime.setText(getTime());
                    break;
                default:
                    break;
            }
        }
    };
    //获得当前年月日时分秒星期
    public String getTime(){
        final Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        String mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
        String mHour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));//时
        String mMinute = String.valueOf(c.get(Calendar.MINUTE));//分
        String mSecond = String.valueOf(c.get(Calendar.SECOND));//秒
        if("1".equals(mWay)){
            mWay ="天";
        }else if("2".equals(mWay)){
            mWay ="一";
        }else if("3".equals(mWay)){
            mWay ="二";
        }else if("4".equals(mWay)){
            mWay ="三";
        }else if("5".equals(mWay)){
            mWay ="四";
        }else if("6".equals(mWay)){
            mWay ="五";
        }else if("7".equals(mWay)){
            mWay ="六";
        }
        return mYear + "年" + mMonth + "月" + mDay+"日"+" "+"星期"+mWay+" "+mHour+":"+mMinute+":"+mSecond;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.clock_test);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("clock", true);
    }

}
