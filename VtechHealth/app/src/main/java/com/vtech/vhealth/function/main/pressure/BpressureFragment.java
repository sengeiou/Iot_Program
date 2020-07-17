package com.vtech.vhealth.function.main.pressure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;

import com.google.gson.reflect.TypeToken;
import com.vtech.vhealth.R;
import com.vtech.vhealth.api.UserAPI;
import com.vtech.vhealth.api.okhttp.BodyCallback;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.base.BaseFragment;
import com.vtech.vhealth.function.bean.ComData;
import com.vtech.vhealth.function.bean.HealthDTOBean;
import com.vtech.vhealth.function.bean.HealthUploadBean;
import com.vtech.vhealth.function.main.MainHealthActivity;
import com.vtech.vhealth.function.main.pressure.calibration.CalibrationActivity;
import com.vtech.vhealth.function.main.pressure.warning.WarningActivity;
import com.vtech.vhealth.function.utils.JsonUtil;
import com.vtech.vhealth.function.utils.LogUtil;
import com.vtech.vhealth.function.utils.NetUtil;
import com.vtech.vhealth.function.utils.PlayVoiceUtil;
import com.vtech.vhealth.function.utils.SpUtils;
import com.vtech.vhealth.function.utils.Utils;
import com.vtech.vhealth.function.widget.EcgView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;

public class BpressureFragment extends BaseFragment<BpressureDelegate> implements View.OnClickListener {
    // tag=1,准备测量，测量中，测量结束
    public final static String TAG1 = "1", TAG2 = "2";
    private final static String STOPACTION = "com.vtech.vhealth.stop.notify";
    private final static int MAXCOUNT = 1;
    private boolean mStop = true;
    private List<HealthDTOBean> mData = new ArrayList<>();
    private List<HealthDTOBean> mRate = new ArrayList<>();
    private int mHeartCount;
    private int mTimeCount;
    private static final String TAG = "BpressureFragment";
    private static final String AUTHORITY = "com.vtech.voiceassistant.comprovider";
    private Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String CASE_GET_COM = "getComdata";
    public static final String KEY_COM_PROVIDER = "comprovider";
    private ComData mComData;
    private String deviceID;

    @Override
    protected Class<BpressureDelegate> getDelegateClass() {
        return BpressureDelegate.class;
    }

    public static BpressureFragment newInstance() {
        BpressureFragment fragment = new BpressureFragment();
        Bundle args = new Bundle();
        //        args.putInt(AnyKey.I_CLIENT_LINK_KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        deviceID= Utils.getDeviceId(getActivity());
        Bundle bundle = getContext().getContentResolver().call(URI, CASE_GET_COM, "", null);
        if (bundle != null) {
            String comDataStr = bundle.getString(KEY_COM_PROVIDER);
            if (!TextUtils.isEmpty(comDataStr)) {
                mComData = JsonUtil.jsonToBean(comDataStr, ComData.class);
            }
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (NetUtil.isConnected(getContext())) {
                    uploadToServer();
                }

            }
        }).start();

    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.addClickListener(this);
        viewDelegate.reSetTag();
    }

    @Override
    public void onResume() {
        super.onResume();
        viewDelegate.updateWarning();
    }



    @Override
    public void onPause() {
        super.onPause();
        clear();
        stopMeasure();
    }

    private void uploadToServer() {
        List<HealthUploadBean> beanList = LitePal.findAll(HealthUploadBean.class);
        if (beanList != null) {
            int size = beanList.size();
            if (size > 10) {
                size = 10;
            }
            for (int i = 0; i < size; i++) {
                uploadToServer(beanList.get(i).getData(), false);
            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pressure_set_calibration:
                CalibrationActivity.start(getContext());
                break;
            case R.id.tv_start:
                String tag = (String) view.getTag();
                viewDelegate.handlerClickUI(tag);
                handlerClick(tag);
                break;
            case R.id.pressure_warning:
                WarningActivity.start(getContext());
                break;
        }

    }

    // 处理语播报
    private void handlerClick(String tag) {
        switch (tag) {
            case BpressureFragment.TAG1:
                //开始测量 1 提示开始测量
                startMeasure();
                PlayVoiceUtil.play(getContext(),getContext().getString(R.string.str_start_measure));
                break;
            case BpressureFragment.TAG2:
                stopMeasure();
                break;

        }
    }

    //开始测量 需要启动数据监听器
    private void startMeasure() {
        MainHealthActivity activity = viewDelegate.getActivity();
        if (activity != null) {
            BpressureUtil.getInstance(activity).start();
            clear();
            mStop = false;
            healthHandler.post(healthRunnable);
        }
    }

    //测量结束需要启动数据监听器
    private void stopMeasure() {
        MainHealthActivity activity = viewDelegate.getActivity();
        if (activity != null) {
            mStop = true;
            BpressureUtil.getInstance(activity).stop();
            viewDelegate.reSetTag();
            healthHandler.removeCallbacks(healthRunnable);
        }
    }

        private void clear() {
            mTimeCount = 0;
            mHeartCount = 0;
            if (viewDelegate != null) {
                EcgView ecgView = viewDelegate.getEcg();
                ecgView.clearData();
                viewDelegate.reSetHealth();
            }
        }

    //数据采集循环周期 1S
    private static final int CYCLE_TIME = 1000;

    //健康handler  用于定时循环
    private Handler healthHandler = new Handler();

    //健康数据 runable
    private Runnable healthRunnable = new Runnable() {
        @Override
        public void run() {
            if (viewDelegate == null) {
                healthHandler.removeCallbacks(healthRunnable);
                return;
            }
            MainHealthActivity activity = viewDelegate.getActivity();
            if (activity != null && !mStop) {
                mTimeCount++;
                ///超过40S 了还没有心率数据，
                LogUtil.show(TAG, "key mTimeCount=" + mTimeCount + "  mHeartCount=" + mHeartCount);
                if (mHeartCount == 0 && mTimeCount > 40) {
                    mTimeCount = 0;
                    PlayVoiceUtil.play(activity, activity.getString(R.string.str_cur_no_data));
                }
//                String key = activity.getHealthData();
                String key = BpressureUtil.getInstance(activity).getHealthData();
                viewDelegate.updateTime();
                LogUtil.show(TAG, "key=" + key);
                if (!TextUtils.isEmpty(key)) {
                    HealthDTOBean healthDTOBean = JsonUtil.jsonToBean(key, HealthDTOBean.class);
                    //暂时先写死 mComData 旧版本 无多用户 弃用了
                    healthDTOBean.setUid("");
                    if (TextUtils.isEmpty(deviceID)) {
                        healthDTOBean.setDeviceId(Utils.getDeviceId(getActivity()));
                    }else {
                        healthDTOBean.setDeviceId(deviceID);
                    }

                    healthDTOBean.setTime(String.valueOf(System.currentTimeMillis()));
//                    LogUtil.show(TAG, "healthDTOBean=" + healthDTOBean.toString());
                    updateView(healthDTOBean, activity);
                    updateEcg(healthDTOBean);
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

    //如果45S后还是没有得到数据那么填充上一次测量的数据
    @Deprecated
    private void autoAddData(HealthDTOBean healthDTOBean) {
        if (mHeartCount >= 60) {
            HealthUploadBean uploadBean = LitePal.findLast(HealthUploadBean.class);
            if (uploadBean != null) {
                String dataStr = uploadBean.getData();
                List<HealthDTOBean> data = JsonUtil.jsonToList(dataStr,
                        new TypeToken<List<HealthDTOBean>>() {
                        });
                if (data != null && data.size() >= 3) {
                    healthDTOBean.setiDBP(data.get(mHeartCount % 3).getiDBP());
                    healthDTOBean.setiSBP(data.get(0).getiSBP());
                }
            }

        }
    }

    private void updateView(HealthDTOBean healthDTOBean, MainHealthActivity activity) {
//        autoAddData(healthDTOBean);
        boolean all = (healthDTOBean.getiDBP() > 0 && healthDTOBean.getiSBP() > 0 && healthDTOBean.getiHRate() > 0);
        if (all) {//有心率,血压全部数据
            handlerWarning(healthDTOBean);
            mData.add(healthDTOBean);
            viewDelegate.updateHealth(mData);
            if (mData.size() >= MAXCOUNT) {//六个数据之内不含本身继续测
                mHeartCount = 0;
                mTimeCount = 0;
                if (viewDelegate != null) {
                    String strRate = viewDelegate.getRate();
                    String strH = viewDelegate.getHigh();
                    String strL = viewDelegate.getLow();
                    for (int i = 0; i <mData.size() ; i++) {
                        HealthDTOBean temp = mData.get(i);
                        temp.setaIHRate(Integer.parseInt(strRate));
                        temp.setaISBP(Integer.parseInt(strH));
                        temp.setaIDBP(Integer.parseInt(strL));
                    }
                }

                uploadToServer(JsonUtil.beanToJson(mData), true);
                //自动结束
                autoStop(activity);
            }
        } else if (healthDTOBean.getiHRate() > 0) {  // 只有心率数据的
            mHeartCount++;
            handlerWarning(healthDTOBean);
            if (handerRate(healthDTOBean)) {
                mRate.add(healthDTOBean);
            }

            viewDelegate.updateHealth(mRate);
            if (mRate.size() > 5) {
                mRate.remove(0);
            }
        }
    }

    //如果当前值超出平均值30 那么过滤掉该值
    private boolean handerRate(HealthDTOBean healthDTOBean) {
        String strRate = viewDelegate.getRate();
        if (!TextUtils.isEmpty(strRate)) {
            int rate = Integer.valueOf(strRate);
            boolean a = (healthDTOBean.getiHRate() - 30 > rate);
            if (rate > 10 && a) {
                return false;
            }
        }
        return true;
    }

    private void autoStop(MainHealthActivity activity) {
        sendStopBroadcast(activity);
        String voice = getString(R.string.str_health_stop,viewDelegate.getHigh(),viewDelegate.getLow(),viewDelegate.getRate());
        PlayVoiceUtil.play(activity, voice);
        mData.clear();
        mRate.clear();
        healthHandler.removeCallbacks(healthRunnable);
        stopMeasure();
    }


    private void sendStopBroadcast(MainHealthActivity activity) {
        activity.sendBroadcast(new Intent(STOPACTION));
    }


    private void updateEcg(HealthDTOBean healthDTOBean) {
        List<Integer> pluse = healthDTOBean.getmPluse();
        EcgView ecgView = viewDelegate.getEcg();
        if (pluse != null) {
            ecgView.setDatas(pluse);
        } else {
            ecgView.clearData();
        }
    }

    private void handlerWarning(HealthDTOBean healthDTOBean) {
        int maxRate = SpUtils.get(AnyKey.KEY_RATE_WARNING, 0);
        MainHealthActivity activity = viewDelegate.getActivity();
        if (activity != null) {
            if (maxRate > 0) {
                if (healthDTOBean.getiHRate() > maxRate) {
                    String sRate = String.format(activity.getString(R.string.str_cur_rate), healthDTOBean.getiHRate(),maxRate);
                    PlayVoiceUtil.play(activity, sRate);
                }
            }
            int bHigh = SpUtils.get(AnyKey.KEY_PRESSUREH_WARNING, 0);
            if (bHigh > 0) {
                if (healthDTOBean.getiSBP() > bHigh) {
                    String sSbp = String.format(activity.getString(R.string.str_cur_sbp), healthDTOBean.getiSBP(),bHigh);
                    PlayVoiceUtil.play(activity, sSbp);
                }
            }
            int bLow = SpUtils.get(AnyKey.KEY_PRESSUREL_WARNING, 0);
            if (bLow > 0) {
                if (healthDTOBean.getiDBP() > bLow) {
                    String sdbp = String.format(activity.getString(R.string.str_cur_bdp), healthDTOBean.getiDBP(),bLow);
                    PlayVoiceUtil.play(activity, sdbp);
                }
            }
        }
    }


    private void uploadToServer(String health, final boolean save) {
        final HealthUploadBean uploadBean = new HealthUploadBean();
        uploadBean.setData(health);
        uploadBean.setStatus(0);
        LogUtil.show(TAG, " 上传 uploadToServer ==" + health);
        UserAPI.addHealths(health, new BodyCallback<String>() {

            @Override
            public void onError(Call call, Exception e, int id) {
                if (save) {
                    uploadBean.save();
                }
                LogUtil.show(TAG, "   onError==" + e.getMessage());
            }


            @Override
            public void onResponse(String response, int id) {
                LogUtil.show(TAG, "   onResponse==" + response);
                if (isSuccess()) {
                    uploadBean.delete();
                }

            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (viewDelegate != null) {
            MainHealthActivity activity = viewDelegate.getActivity();
            if (activity != null) {
                BpressureUtil.getInstance(activity).stop();
            }
        }

        if (healthHandler != null) {
            healthHandler.removeCallbacks(healthRunnable);
        }
    }

}
