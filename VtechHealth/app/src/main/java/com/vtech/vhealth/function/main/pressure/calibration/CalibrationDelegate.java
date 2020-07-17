package com.vtech.vhealth.function.main.pressure.calibration;

import android.app.Activity;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vtech.vhealth.R;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.base.AppDelegate;
import com.vtech.vhealth.function.utils.SpUtils;
import com.vtech.vhealth.function.widget.CustomTitleBar;

public class CalibrationDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_calibration;
    }

    @Override
    public void initWidget() {
        super.initWidget();
        CustomTitleBar titleBar = getTitle();
        titleBar.setTextCenter(getRootView().getContext().getString(R.string.calibration));
        titleBar.setTextRight(getRootView().getContext().getString(R.string.clear));
        titleBar.hideTextLeft(true);
        intEditText();
        get(R.id.tv_calibration).setTag(false);
    }

    public void intEditText() {
        InputFilter[] inputFilters = new InputFilter[]{new InputFilterMinMax("0", "255")};
        EditText pHigh = getPhigh();
        pHigh.setFilters(inputFilters);
        EditText pLow = getPLow();
        pLow.setFilters(inputFilters);

        EditText pRate = getRate();
        pRate.setFilters(inputFilters);

        int rate = SpUtils.get(AnyKey.KEY_RATE_CALIBRATION, 0);
        int pressureH = SpUtils.get(AnyKey.KEY_PRESSUREH_CALIBRATION, 0);
        int pressureL = SpUtils.get(AnyKey.KEY_PRESSUREL_CALIBRATION, 0);
        if (rate != 0) {
            pRate.setText(String.valueOf(rate));
        }
        if (pressureH != 0) {
            pHigh.setText(String.valueOf(pressureH));
        }
        if (pressureL != 0) {
            pLow.setText(String.valueOf(pressureL));
        }
    }

    public void setOnClick(View.OnClickListener listener) {
        getTitle().setOnClickRightViewListener(listener);
        getTitle().setOnClickRightTextListener(listener);
//        getTitle().setOnClickRightTextListener(listener);
        CustomTitleBar titleBar = getTitle();
        titleBar.setOnClickLeftViewListener(listener);
        setOnClickListener(listener, R.id.tv_calibration);

    }

    public void clearData() {
        getPhigh().setText("");
        getPLow().setText("");
        getRate().setText("");
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

    public void update(boolean calibration) {
        Activity activity = getActivity();
        if (activity != null && !activity.isFinishing() && !activity.isDestroyed()) {
            boolean r = TextUtils.isEmpty(getRate().getText().toString().trim());
            boolean h = TextUtils.isEmpty(getPhigh().getText().toString().trim());
            boolean l = TextUtils.isEmpty(getPLow().getText().toString().trim());
            if (!r && !h && !l) {
                TextView textView = get(R.id.tv_calibration);
                if (calibration) {
                    textView.setText(activity.getText(R.string.str_start_calibrating));
                } else {
                    textView.setText(activity.getText(R.string.str_wait_calibrating));
                }
            }

        }

    }

}
