package com.vtech.check.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.vtech.check.R;

public class BatteryTest extends BaseTest{

    View rootview;
    private TextView mTvVoltage;
    private TextView mTvTemperature;
    private TextView mTvLevel;
    private TextView mTvStatus;
    private TextView mTvHealth;
    private TextView mTvTechnology;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            rootview=inflater.inflate(R.layout.battery,container,false);
        initView();
        return rootview;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    private void initView() {
        mTvVoltage = (TextView)rootview.findViewById(R.id.tv_voltage);
        mTvTemperature = (TextView)rootview.findViewById(R.id.tv_temperature);
        mTvLevel = (TextView)rootview.findViewById(R.id.tv_level);
        mTvStatus = (TextView)rootview.findViewById(R.id.tv_status);
        mTvHealth = (TextView)rootview.findViewById(R.id.tv_health);
        mTvTechnology = (TextView)rootview.findViewById(R.id.tv_technology);

        getActivity().registerReceiver(this.mBatteryReceiver, new IntentFilter(
                Intent.ACTION_BATTERY_CHANGED));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(this.mBatteryReceiver);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.battery_test);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("battery",true);
    }



    private BroadcastReceiver mBatteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context arg0, Intent arg1) {
            int voltage=arg1.getIntExtra(BatteryManager.EXTRA_VOLTAGE, 0);
            mTvVoltage.setText("电压：" + voltage / 1000 + "." + voltage % 1000 + "V");

            int temperature=arg1.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
            mTvTemperature.setText("温度：" + temperature / 10 + "." + temperature % 10 + "℃");
            if (temperature >= 300) {
                mTvTemperature.setTextColor(Color.RED);
            } else {
                mTvTemperature.setTextColor(Color.BLUE);
            }

            int level=arg1.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
            int scale=arg1.getIntExtra(BatteryManager.EXTRA_SCALE,0);
            int levelPercent = (int)(((float)level / scale) * 100);
            mTvLevel.setText("电量：" + levelPercent + "%");
            if (level <= 10) {
                mTvLevel.setTextColor(Color.RED);
            } else {
                mTvLevel.setTextColor(Color.BLUE);
            }

            int status = arg1.getIntExtra(BatteryManager.EXTRA_STATUS, BatteryManager.BATTERY_STATUS_UNKNOWN);
            String strStatus = "未知状态";;
            switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING:
                    strStatus = "充电中……";
                    break;
                case BatteryManager.BATTERY_STATUS_DISCHARGING:
                    strStatus = "放电中……";
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING:
                    strStatus = "未充电";
                    break;
                case BatteryManager.BATTERY_STATUS_FULL:
                    strStatus = "充电完成";
                    break;
            }
            mTvStatus.setText("状态：" + strStatus);

            int health = arg1.getIntExtra(BatteryManager.EXTRA_HEALTH, BatteryManager.BATTERY_HEALTH_UNKNOWN);
            String strHealth = "未知 :(";;
            switch (status) {
                case BatteryManager.BATTERY_HEALTH_GOOD:
                    strHealth = "好 :)";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVERHEAT:
                    strHealth = "过热！";
                    break;
                case BatteryManager.BATTERY_HEALTH_DEAD: // 未充电时就会显示此状态，这是什么鬼？
                    strHealth = "良好";
                    break;
                case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE:
                    strHealth = "电压过高！";
                    break;
                case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:
                    strHealth = "未知 :(";
                    break;
                case BatteryManager.BATTERY_HEALTH_COLD:
                    strHealth = "过冷！";
                    break;
            }
            mTvHealth.setText("健康状况：" + strHealth);

            String technology = arg1.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
            mTvTechnology.setText("电池技术：" + technology);
        }
    };




}
