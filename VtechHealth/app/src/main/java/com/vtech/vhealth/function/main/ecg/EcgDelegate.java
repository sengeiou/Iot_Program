package com.vtech.vhealth.function.main.ecg;

import android.view.View;
import android.widget.CheckBox;

import com.vtech.vhealth.R;
import com.vtech.vhealth.function.base.AppDelegate;

public class EcgDelegate extends AppDelegate {
    @Override
    public int getRootLayoutId() {
        return R.layout.fragment_seria;
    }



    public void addClickListener(View.OnClickListener listener) {
        setOnClickListener(listener,
                R.id.startButton ,
                R.id.stopButton ,
                R.id.setAlgosButton ,
                R.id.ecgGraph
        );
    }

    public CheckBox getEcgHeartrate(){
        return get(R.id.ecgHeartrate);
    }

    public void setSqText(String sqText){
//        setText(R.id.sqText,sqText);
    }

    //实时 和平均 心率 血压值
    public void update() {

        setText(R.id.pressure_low_avg, "");//平均低压
    }

    public void setWarning(String warning) {
        setText(R.id.pressure_warning, warning);//报警阈值
    }
}
