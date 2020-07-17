package com.vtech.vhealth.function.main.pressure.warning;

import android.app.Activity;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;

import com.vtech.vhealth.R;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.base.AppDelegate;
import com.vtech.vhealth.function.main.pressure.calibration.InputFilterMinMax;
import com.vtech.vhealth.function.utils.SpUtils;
import com.vtech.vhealth.function.widget.CustomTitleBar;

public class WarningDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_warning;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        CustomTitleBar titleBar = getTitle();
        titleBar.setTextCenter(getRootView().getContext().getText(R.string.warning_value));
        titleBar.setImageRight(R.drawable.icon_save);
        titleBar.showRightView();
        titleBar.setOnClickLeftViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity=getActivity();
                if (activity != null) {
                    activity.finish();
                }
            }
        });
        titleBar.hideTextLeft(true);
        updateWarning();
        intEditText();
    }

    public void intEditText() {
        getPhigh().setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        getPLow().setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
        getRate().setFilters(new InputFilter[]{ new InputFilterMinMax("0", "255")});
    }

    public void updateWarning() {
        int rate = SpUtils.get(AnyKey.KEY_RATE_WARNING, 0);
        int pressureH = SpUtils.get(AnyKey.KEY_PRESSUREH_WARNING, 0);
        int pressureL = SpUtils.get(AnyKey.KEY_PRESSUREL_WARNING, 0);
        if (rate != 0) {
            getRate().setText(String.valueOf(rate));
        }
        if (pressureH != 0) {
            getPhigh().setText(String.valueOf(pressureH));
        }
        if (pressureL != 0) {
            getPLow().setText(String.valueOf(pressureL));
        }


    }


    public void setOnClick(View.OnClickListener listener) {
        getTitle().setOnClickRightViewListener(listener);
        getTitle().setOnClickRightTextListener(listener);
    }

    public CustomTitleBar getTitle() {
        return get(R.id.title_bar);
    }

    public EditText getPhigh() {

        return get(R.id.et_pressure_high);
    }

    public EditText getPLow() {
        return get(R.id.et_pressure_low);
    }

    public EditText getRate() {
        return get(R.id.et_rate);
    }

}
