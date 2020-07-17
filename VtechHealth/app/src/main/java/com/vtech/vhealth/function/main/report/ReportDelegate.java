package com.vtech.vhealth.function.main.report;

import android.view.View;

import com.vtech.vhealth.R;
import com.vtech.vhealth.function.base.AppDelegate;

public class ReportDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_report;
    }

    public void addClickListener(View.OnClickListener listener) {
        setOnClickListener(listener, R.id.tv_start);
    }

    //实时 和平均 心率 血压值
    public void update() {

        setText(R.id.pressure_real_time, "");//实时时间
        setText(R.id.health_rate, "");//实时心率
        setText(R.id.pressure_high, "");//实时高压
        setText(R.id.pressure_low, "");//实时低压
        setText(R.id.health_rate_avg, "");//平均心率
        setText(R.id.pressure_high_avg, "");//平均高压
        setText(R.id.pressure_low_avg, "");//平均低压
    }

    public void setWarning(String warning) {
        setText(R.id.pressure_warning, warning);//报警阈值
    }

}
