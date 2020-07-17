package com.vtech.vhealth.function.main.pressure.warning;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.vtech.vhealth.R;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.base.BaseActivity;
import com.vtech.vhealth.function.utils.SpUtils;

public class WarningActivity extends BaseActivity<WarningDelegate> implements View.OnClickListener {

    @Override
    protected Class<WarningDelegate> getDelegateClass() {
        return WarningDelegate.class;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, WarningActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClick(this);
    }

    @Override
    public void onClick(View view) {
        String rate = viewDelegate.getRate().getText().toString().trim();
        String phigh = viewDelegate.getPhigh().getText().toString().trim();
        String plow = viewDelegate.getPLow().getText().toString().trim();
        if (TextUtils.isEmpty(rate)) {
            viewDelegate.toastShort(getString(R.string.str_intput_rate));
            return;
        }
        if (TextUtils.isEmpty(phigh)) {
            viewDelegate.toastShort(getString(R.string.str_intput_sbp));
            return;
        }
        if (TextUtils.isEmpty(plow)) {
            viewDelegate.toastShort(getString(R.string.str_input_bdp));
            return;
        }
        SpUtils.set(AnyKey.KEY_RATE_WARNING,Integer.parseInt(rate));
        SpUtils.set(AnyKey.KEY_PRESSUREH_WARNING, Integer.parseInt(phigh));
        SpUtils.set(AnyKey.KEY_PRESSUREL_WARNING,Integer.parseInt(plow));
        SpUtils.getSp().edit().commit();
        finish();

    }
}
