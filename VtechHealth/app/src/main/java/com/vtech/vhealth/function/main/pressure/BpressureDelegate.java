package com.vtech.vhealth.function.main.pressure;

import android.view.View;
import android.widget.TextView;

import com.vtech.vhealth.R;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.base.AppDelegate;
import com.vtech.vhealth.function.bean.HealthDTOBean;
import com.vtech.vhealth.function.utils.SpUtils;
import com.vtech.vhealth.function.utils.TimeUtil;
import com.vtech.vhealth.function.widget.EcgView;

import java.util.List;

public class BpressureDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_bpressure;
    }

    public void addClickListener(View.OnClickListener listener) {
        setOnClickListener(listener, R.id.tv_start, R.id.pressure_warning, R.id.pressure_set_calibration);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        updateWarning();
    }

    public EcgView getEcg(){
        return get(R.id.ecg_pulse);
    }

    public void updateTime(){
        setText(R.id.pressure_real_time, TimeUtil.getCurTime());//实时时间
    }

    public void reSetHealth(){
        setText(R.id.health_rate_avg, String.valueOf(0));//平均心率
        setText(R.id.pressure_high_avg,  String.valueOf(0));//平均高压
        setText(R.id.pressure_low_avg, String.valueOf(0));//平均低压
        setText(R.id.pressure_high, String.valueOf(0));//实时高压
        setText(R.id.pressure_low, String.valueOf(0));//实时低压
        setText(R.id.health_rate, String.valueOf(0));//实时心率
    }

    //实时 和平均 心率 血压值
    public void updateHealth(List<HealthDTOBean> data) {

        int size = data.size();
        if (size > 0) {
            //计算平均值
            int sumH = 0, sumL = 0, sumR = 0;
            for (int i = 0; i < size; i++) {
                HealthDTOBean avgBean = data.get(i);
                if (avgBean != null  ) {
                    sumH += avgBean.getiSBP();
                    sumL +=avgBean.getiDBP();
                    sumR += avgBean.getiHRate();
                }
            }
            setText(R.id.health_rate_avg, String.valueOf(sumR / size) );//平均心率
            setText(R.id.pressure_high_avg,  String.valueOf(sumH / size ));//平均高压
            setText(R.id.pressure_low_avg,  String.valueOf(sumL / size ));//平均低压

            //设置实时值 并设置平均值
            HealthDTOBean realTime = data.get(size - 1);
            if (realTime != null) {
                setText(R.id.pressure_high, String.valueOf( realTime.getiSBP()));//实时高压
                setText(R.id.pressure_low, String.valueOf( realTime.getiDBP()));//实时低压
                setText(R.id.health_rate, String.valueOf( realTime.getiHRate()));//实时心率
                realTime.setaIDBP(sumL);
                realTime.setaISBP(sumH);
                realTime.setaIHRate(sumR);
            }
        }
    }

    public String getRate(){
        return getTextView(R.id.health_rate_avg).getText().toString().trim();
    }
    public String getHigh(){
        return getTextView(R.id.pressure_high_avg).getText().toString().trim();
    }
    public String getLow(){
        return getTextView(R.id.pressure_low_avg).getText().toString().trim();
    }

    //测量结束后恢复按钮初始状态
    public void reSetTag() {
        TextView start = get(R.id.tv_start);
        if (start == null) {
            return;
        } else {
            start.setText(start.getResources().getString(R.string.str_measure));
            start.setTag(BpressureFragment.TAG1);
        }
    }

    public void handlerClickUI(String tag) {
        TextView start = get(R.id.tv_start);
        if (start == null) {
            return;
        }
        switch (tag) {
            case BpressureFragment.TAG1://开始测量
                start.setText(start.getResources().getString(R.string.str_measuring));
                start.setTag(BpressureFragment.TAG2);
                break;
            case BpressureFragment.TAG2:
//                toastShort("正在测量中，请稍候...");
                break;
        }
    }

    public void updateWarning() {
        int rate = SpUtils.get(AnyKey.KEY_RATE_WARNING, 0);
        int pressureH = SpUtils.get(AnyKey.KEY_PRESSUREH_WARNING, 0);
        int pressureL = SpUtils.get(AnyKey.KEY_PRESSUREL_WARNING, 0);
        if (rate == 0 || pressureH == 0 || pressureL == 0) {
            setText(R.id.pressure_warning, getActivity().getString(R.string.str_set_warning));//报警阈值
        } else {
            String warningSet=String.format(getActivity().getString(R.string.str_cur_set),pressureH,pressureL,rate);
            setText(R.id.pressure_warning, warningSet);//报警阈值
        }
    }
}
