package com.vtech.check.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.vtech.check.R;
import com.vtech.check.activity.ItemActivity;
import com.vtech.check.function.main.pressure.BpressureUtil;

public class BloodTest extends BaseTest{

    View rootview;
    TextView btn_bood;
    TextView bood_text;


    private int mTimeCount;
    private int mHeartCount;
    private boolean mStop = true;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            rootview=inflater.inflate(R.layout.blood,container,false);
        initView();
        setButtonVisibility(true);
        return rootview;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }


    private void initView() {
         btn_bood=rootview.findViewById(R.id.btn_bood);
         bood_text=rootview.findViewById(R.id.bood_text);
         bood_text.setMovementMethod(ScrollingMovementMethod.getInstance());
         btn_bood.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 startMeasure();
             }
         });

    }

    private void clear() {
        mTimeCount = 0;
        mHeartCount = 0;
        bood_text.setText("");
    }

    //数据采集循环周期 1S
    private static final int CYCLE_TIME = 1000;

    //健康handler  用于定时循环
    private Handler healthHandler = new Handler();

    //健康数据 runable
    private Runnable healthRunnable = new Runnable() {
        @Override
        public void run() {

            ItemActivity activity = (ItemActivity) getActivity();
            if (activity != null ) {
                mTimeCount++;
                ///超过40S 了还没有心率数据，
//                LogUtil.show(TAG, "key mTimeCount=" + mTimeCount + "  mHeartCount=" + mHeartCount);
                if (mHeartCount == 0 && mTimeCount > 40) {
                    mTimeCount = 0;
                    Toast.makeText(getContext(),"当前还没有采集到数据，请调整手指继续测量",Toast.LENGTH_SHORT).show();
                }

                String key = BpressureUtil.getInstance(activity).getHealthData();

                if (!TextUtils.isEmpty(key)) {

                    bood_text.append(key+"\n");

            /*        HealthDTOBean healthDTOBean = JsonUtil.jsonToBean(key, HealthDTOBean.class);
                    //暂时先写死 mComData todo mComData 写入真实数据
                    healthDTOBean.setUid("");
                    if (TextUtils.isEmpty(deviceID)) {
                        healthDTOBean.setDeviceId(Utils.getDeviceId(getActivity()));
                    }else {
                        healthDTOBean.setDeviceId(deviceID);
                    }

                    healthDTOBean.setTime(String.valueOf(System.currentTimeMillis()));
//                    LogUtil.show(TAG, "healthDTOBean=" + healthDTOBean.toString());
                    updateView(healthDTOBean, activity);
                    updateEcg(healthDTOBean);*/
                    healthHandler.postDelayed(this, CYCLE_TIME);
                } else {
                    stopMeasure();
                    healthHandler.removeCallbacks(healthRunnable);
                }

            } else {
                healthHandler.removeCallbacks(healthRunnable);
            }
        }
    };

    //开始测量 需要启动数据监听器
    private void startMeasure() {
        ItemActivity activity = (ItemActivity) getActivity();
        if (activity != null) {
            BpressureUtil.getInstance(activity).start();
            clear();
            mStop = false;
            healthHandler.post(healthRunnable);
        }
    }

    //测量结束需要启动数据监听器
    private void stopMeasure() {
        ItemActivity activity = (ItemActivity) getActivity();
        if (activity != null) {
            mStop = true;
            BpressureUtil.getInstance(activity).stop();
//            viewDelegate.reSetTag();
            healthHandler.removeCallbacks(healthRunnable);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        clear();
        stopMeasure();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (healthHandler != null) {
            healthHandler.removeCallbacks(healthRunnable);
        }
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.blood_test);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("blood",true);
    }
}
