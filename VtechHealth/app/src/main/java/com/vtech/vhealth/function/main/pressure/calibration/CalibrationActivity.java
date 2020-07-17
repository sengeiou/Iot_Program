package com.vtech.vhealth.function.main.pressure.calibration;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.vtech.vhealth.R;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.base.BaseActivity;
import com.vtech.vhealth.function.bean.HealthDTOBean;
import com.vtech.vhealth.function.main.pressure.BpressureUtil;
import com.vtech.vhealth.function.utils.JsonUtil;
import com.vtech.vhealth.function.utils.LogUtil;
import com.vtech.vhealth.function.utils.SpUtils;

public class CalibrationActivity extends BaseActivity<CalibrationDelegate> implements View.OnClickListener {
    private static final String TAG="CalibrationActivity";
    private int calibrationStatus;
    private int clearCalibration = -1;

    private int rate, high, low;
    //时间周期
    private int timeCount;
    private static final int CLEARTIME = 1;
    private static final int FALSELTIME = 4;

    //数据采集循环周期 1S
    private static final int CYCLE_TIME = 1000;
    //是否正在擦除状态
    private boolean isClear = false;

    //健康handler  用于定时循环
    private Handler healthHandler = new Handler();

    @Override
    protected Class<CalibrationDelegate> getDelegateClass() {
        return CalibrationDelegate.class;
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, CalibrationActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClick(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_left:
                healthHandler.removeCallbacks(healthRunnable);
                stop();
                finish();
                break;
            case R.id.layout_right:
            case R.id.tv_right:
                clearCalibration();
                healthHandler.post(healthRunnable);
                break;
            case R.id.tv_calibration:
                boolean tag = (boolean) view.getTag();
                viewDelegate.update(tag);
                if (tag) {
                    healthHandler.removeCallbacks(healthRunnable);
                    stop();
                    view.setTag(false);
                } else {
                    handleCalibration();
                    view.setTag(true);
                }
                break;

        }


    }

    private void clearCalibration() {
        //清除
        isClear = true;
        BpressureUtil.getInstance(CalibrationActivity.this).start();
    }

    private void handleCalibration() {
        //开始校准
        String prate = viewDelegate.getRate().getText().toString().trim();
        String phigh = viewDelegate.getPhigh().getText().toString().trim();
        String plow = viewDelegate.getPLow().getText().toString().trim();
        if (TextUtils.isEmpty(phigh)) {
            viewDelegate.toastShort(getResources().getString(R.string.str_please_intput_rate));
            return;
        }
        if (TextUtils.isEmpty(plow)) {
            viewDelegate.toastShort(getResources().getString(R.string.str_please_intput_sbp));
            return;
        }
        if (TextUtils.isEmpty(prate)) {
            viewDelegate.toastShort(getResources().getString(R.string.str_please_intput_bdp));
            return;
        }

        rate = Integer.valueOf(prate);
        high = Integer.valueOf(phigh);
        low = Integer.valueOf(plow);

        BpressureUtil.getInstance(CalibrationActivity.this).start();
        healthHandler.post(healthRunnable);
        isClear = false;

    }


    //十进制字符串 转16进制 byte
    private static byte intToHex(int value) {
        ///十进制转16
        String strHex = Integer.toHexString(value);
        ///16进制字符串转 byte  strHex 范围为0x00到0xFF
        return (byte) (0xff & Integer.parseInt(strHex, 16));
    }


    //健康数据 runable
    private Runnable healthRunnable = new Runnable() {
        @Override
        public void run() {
            String key = BpressureUtil.getInstance(CalibrationActivity.this).getHealthData();
            LogUtil.show(TAG, "key=" + key);
            timeCount++;
            if (!TextUtils.isEmpty(key)) {
                HealthDTOBean healthDTOBean = JsonUtil.jsonToBean(key, HealthDTOBean.class);
                calibrationStatus = healthDTOBean.getCalibrationStatus();
                clearCalibration = healthDTOBean.getClearCalibration();
                ///6S之后发送 校准数据 有心率数据之后
                if (!isClear && healthDTOBean.getiHRate() > 0 && (high != 0 && low != 0 & rate != 0)) {
                    byte[] calibration = new byte[4];
                    calibration[0] = (byte) (0xff & 0xfe);
                    calibration[1] = intToHex(high);
                    calibration[2] = intToHex(low);
                    calibration[3] = intToHex(rate);
                    BpressureUtil.getInstance(CalibrationActivity.this).setCalionration(calibration);
                }

                LogUtil.show(TAG, "timeCount=" + timeCount);
                switch (calibrationStatus) {
                    case 0://成功
                        LogUtil.show(TAG, "校准成功=");
                        viewDelegate.update(true);
                        showCalibrationDialog(getString(R.string.str_calibration_success), 0);
                        saveCalibration(rate, high, low);
                        healthHandler.removeCallbacks(healthRunnable);
                        stop();
                        return;
                    case 1://校准中
                        LogUtil.show(TAG, "校准中=");
                        break;
                    case 2://失败
                        LogUtil.show(TAG, "校准失败=");
                        viewDelegate.update(true);
                        showCalibrationDialog(getString(R.string.str_calibration_fail), 1);
                        healthHandler.removeCallbacks(healthRunnable);
                        stop();
                        return;
                }
                ///当前是否处于擦除状态
                if (isClear) {
                    if (timeCount == CLEARTIME) {
                        BpressureUtil.getInstance(CalibrationActivity.this).sendClearCalion();
                    }
                    if (clearCalibration == 0) {
//                    清除校准数据 成功
                        LogUtil.show(TAG, "清除校准数据 成功=");
                        viewDelegate.clearData();
                        showCalibrationDialog(getString(R.string.str_clear_success),3);
                        SpUtils.set(AnyKey.KEY_RATE_CALIBRATION, 0);
                        SpUtils.set(AnyKey.KEY_PRESSUREH_CALIBRATION, 0);
                        SpUtils.set(AnyKey.KEY_PRESSUREL_CALIBRATION, 0);
//                        showClearStatus(0);
                        healthHandler.removeCallbacks(healthRunnable);
                        stop();
                        return;
                    }
//                什么时候算失败？
                    if ((clearCalibration == -1) && timeCount >= FALSELTIME) {
                        if (timeCount - FALSELTIME >= FALSELTIME) {
                            LogUtil.show(TAG, "清除校准数据 失败 =");
//                            showClearStatus(1);
                            healthHandler.removeCallbacks(healthRunnable);
                            stop();
                            showCalibrationDialog(getString(R.string.str_clear_fail),4);
                            return;
                        } else {
                            BpressureUtil.getInstance(CalibrationActivity.this).sendClearCalion();
                        }

                    }
                }

                healthHandler.postDelayed(healthRunnable, CYCLE_TIME);
            } else {
                healthHandler.removeCallbacks(healthRunnable);
                stop();
                return;
            }
        }
    };

    private void saveCalibration(int rate, int high, int low) {
        SpUtils.set(AnyKey.KEY_RATE_CALIBRATION, rate);
        SpUtils.set(AnyKey.KEY_PRESSUREH_CALIBRATION, high);
        SpUtils.set(AnyKey.KEY_PRESSUREL_CALIBRATION, low);
        SpUtils.getSp().edit().commit();
    }

    private void showCalibrationDialog(String title, final int status) {
        LogUtil.show(TAG, " 校准结果 showCalibrationDialog =" + title);
        CalibrationDialog calibrationDialog = new CalibrationDialog(this, R.style.customDialog)
                .setPositiveButton(getResources().getString(R.string.str_sure), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (status == 1) {
                            handleCalibration();
                        }
                    }
                });
        calibrationDialog.setTitle(title);
        calibrationDialog.showDialog();
    }

    //
    private void showClearStatus(int status) {
        LogUtil.show(TAG, " showClearStatus=" + status);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stop();
    }

    private void stop() {
        timeCount = 0;
        healthHandler.removeCallbacks(healthRunnable);
        BpressureUtil.getInstance(CalibrationActivity.this).stop();
    }
}
