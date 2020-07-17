package com.vtech.check.serfragment.ecg;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.google.gson.Gson;
import com.neurosky.AlgoSdk.NskAlgoDataType;
import com.neurosky.AlgoSdk.NskAlgoECGValueType;
import com.neurosky.AlgoSdk.NskAlgoProfile;
import com.neurosky.AlgoSdk.NskAlgoSampleRate;
import com.neurosky.AlgoSdk.NskAlgoSdk;
import com.neurosky.AlgoSdk.NskAlgoSignalQuality;
import com.neurosky.AlgoSdk.NskAlgoState;
import com.neurosky.AlgoSdk.NskAlgoType;
import com.vtech.check.fragment.BaseTest;
import com.vtech.check.serfragment.BaseFragment;



import com.vtech.check.R;

import com.vtech.check.function.bean.EcgBean;

import com.vtech.check.serial.SerialPortUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



/****
 * @author jason
 *心电页面
 * */
public class EcgFragment extends BaseFragment<EcgDelegate> implements View.OnClickListener {
    final String TAG = "SerialActivityTag";

    SerialPortUtil mSerial;

    // graph plot variables
    private final static int X_RANGE = 50;
    private int mCurrentXRange = X_RANGE;
    private SimpleXYSeries ecgSeries = null;

    // internal variables
    private boolean bInited = false;
    private boolean bRunning = false;
    private int currentSelectedAlgo;

    // canned data variables
    private short raw_data[] = {0};
    private int raw_data_index = 0;
    private float output_data[];
    private int output_data_count = 0;
    private int raw_data_sec_len = 85;

    private int hr = -1;
    private int robust = -1;
    private int mood = -1;
    private int r2r = -1;
    private int hrv = -1;
    private int heartage = -1;
    private int afib = -1;
    private int rdetected = -1;
    private int stress = -1;
    private int heartbeat = -1;
    private int resp = -1;

    /**
     * UI components
     */
    private XYPlot plot;
    private Button setAlgosButton;
    private Button startButton;
    private Button stopButton;
    private Button ecgText;
    private CheckBox ecgHeartrate;
    private CheckBox ecgStress;
    private CheckBox ecgSmooth;
    private CheckBox ecgHRV;
    private CheckBox ecgAfib;
    private CheckBox ecgMood;
    private CheckBox ecgHeartage;
    private CheckBox ecgResp;
    private CheckBox ecgHRVTD;
    private CheckBox ecgHRVFD;
    private TextView stateText;
    private TextView sqTitle;
    private TextView ecgStatus;
    private TextView ecgStatus2;


    TextView hfLfTv;

    TextView pnnTv;

    TextView sdnnTv;

    TextView afibTv;

    TextView hrvTvs;

    TextView moodTv;

    TextView heartageTv;

    TextView hrTv;

    TextView stressTv;

    private int activeProfile = -1;
    /**
     * indicates waiting for the final quality information
     */
    private boolean bWaitQ = false;
    /**
     * indicate the overall quality request is sent
     */
    private boolean bSendQ = false;

    /**
     * The ECG SDK instance
     */
    private NskAlgoSdk nskAlgoSdk;

    /**
     * ECG SDK Release license
     */
    private String license = "";

    @Override
    protected Class<EcgDelegate> getDelegateClass() {
        return EcgDelegate.class;
    }

    public static EcgFragment newInstance() {
        EcgFragment fragment = new EcgFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    private void initView() {
        startButton = (Button) viewDelegate.getRootView().findViewById(R.id.startButton);
        stopButton = (Button) viewDelegate.getRootView().findViewById(R.id.stopButton);
        setAlgosButton = (Button) viewDelegate.getRootView().findViewById(R.id.setAlgosButton);
        ecgText = (Button) viewDelegate.getRootView().findViewById(R.id.ecgGraph);
        ecgHeartrate = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgHeartrate);
        ecgAfib = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgAfib);
        ecgHeartage = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgHeartage);
        ecgHRV = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgHRV);
        ecgMood = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgMood);
        ecgResp = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgResp);
        ecgStress = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgStress);
        ecgSmooth = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgSmooth);
        ecgHRVTD = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgHRVTD);
        ecgHRVFD = (CheckBox) viewDelegate.getRootView().findViewById(R.id.ecgHRVFD);
        stateText = (TextView) viewDelegate.getRootView().findViewById(R.id.stateText);
        sqTitle = (TextView) viewDelegate.getRootView().findViewById(R.id.sqTitle);


        hfLfTv = (TextView) viewDelegate.getRootView().findViewById(R.id.hf_lf_tv);
        pnnTv = (TextView) viewDelegate.getRootView().findViewById(R.id.pnn_tv);
        sdnnTv = (TextView) viewDelegate.getRootView().findViewById(R.id.sdnn_tv);

        afibTv = (TextView) viewDelegate.getRootView().findViewById(R.id.afib_tv);
        hrvTvs = (TextView) viewDelegate.getRootView().findViewById(R.id.hrv_tvs);
        moodTv = (TextView) viewDelegate.getRootView().findViewById(R.id.mood_tv);
        heartageTv = (TextView) viewDelegate.getRootView().findViewById(R.id.heartage_tv);
        hrTv = (TextView) viewDelegate.getRootView().findViewById(R.id.hr_tv);
        stressTv = (TextView) viewDelegate.getRootView().findViewById(R.id.stress_tv);

        ecgStatus = (TextView) viewDelegate.getRootView().findViewById(R.id.ecgStatus);
        ecgStatus2 = (TextView) viewDelegate.getRootView().findViewById(R.id.ecgStatus2);

        viewDelegate.getRootView().findViewById(R.id.question1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EcgHrvAlertDialog(getActivity(), R.style.customDialog)
//                        .setTitle(message)
                        .setPositiveButton(getActivity().getString(R.string.str_sure), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).showDialog();
            }
        });

        viewDelegate.getRootView().findViewById(R.id.question2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new EcgAlertDialog(getActivity(), R.style.customDialog)
//                        .setTitle(message)
                        .setPositiveButton(getActivity().getString(R.string.str_sure), new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                            }
                        }).showDialog();
            }
        });

    }

    boolean hasData = false;

    private CountDownTimer cdTimer = new CountDownTimer(2 * 60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished == 2 * 59 * 1000) {
                nskAlgoSdk.NskAlgoQueryOverallQuality(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG);
                Log.i(TAG, "startSendECGThread NskAlgoQueryOverallQuality");
            }
        }

        @Override
        public void onFinish() {
            if (bRunning) {
                nskAlgoSdk.NskAlgoStop();
                mSerial.setStop();
                player(getActivity().getString(R.string.ecg_measurement_completed));
                hasData = false;
                if (chartData.size() > 0 && chartData.size() > 1000) {
                    chartData = chartData.subList(chartData.size() - 1000, chartData.size());

                    String data = new Gson().toJson(chartData);
                    Log.e(TAG, "chartData size == " + chartData.size() + " , chardata is " + data);
                    EcgBean bean = new EcgBean();
                    bean.setEcgData(data);
                    bean.save();
//                    LitePal.find(EcgBean.class,1);
//                    LitePal.findLast()
                    sendChartBroadcast(data);

                    uploadToServer(data);
                }
            }
        }
    };

    private void uploadToServer(String ecg) {
        Log.i(TAG, " 上传 uploadToServer ==" + ecg);
/*        UserAPI.addEcgData(Utils.getDeviceId(getActivity()), ecg,
                sdnnTv.getText().toString().substring(sdnnTv.getText().toString().indexOf("[")),
                hfLfTv.getText().toString().substring(hfLfTv.getText().toString().indexOf("[")),
                pnnTv.getText().toString().substring(pnnTv.getText().toString().indexOf("[")),
                String.valueOf(afib), String.valueOf(heartage), String.valueOf(hrv),
                String.valueOf(hr), String.valueOf(mood), String.valueOf(stress),
                new BodyCallback<String>() {

                    @Override
                    public void onError(Call call, Exception e, int id) {

                        Log.i(TAG, "   onError==" + e.getMessage());
                    }


                    @Override
                    public void onResponse(String response, int id) {
                        Log.i(TAG, "   onResponse==" + response);
                        if (isSuccess()) {
                            ToastUtil.showShort("心电数据上传成功");
                        }
                    }
                });*/
    }

    private void sendChartBroadcast(String data) {
        Intent intent = new Intent("com.vtech.vhealth.heart.stop.notify");
        intent.putExtra("chart", data);
        getActivity().sendBroadcast(intent);
    }

    private void player(String str) {
    /*    MainHealthActivity activity = viewDelegate.getActivity();
        if (activity != null) {
            PlayVoiceUtil.play(activity, str);
        }*/
    }

    List<Integer> chartData = new ArrayList<>();

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.addClickListener(this);

        nskAlgoSdk = new NskAlgoSdk();

        initView();

        loadsetup();

        mSerial = new SerialPortUtil(SerialPortUtil.RESPIRE_DEV_PATH);

        mSerial.setOnDataReceiveListener(serialReceiveListener);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bRunning) {
                    nskAlgoSdk.NskAlgoStart(false);
                    mSerial.onCreate();
                    mSerial.setStart();
                    player("请放好手指正在采集数据");

                    if (!hasData) {
                        hasData = true;
                        cdTimer.start();
                    }
                } else {
                    nskAlgoSdk.NskAlgoStop();
                    mSerial.setStop();
                    if (cdTimer != null) {
                        cdTimer.cancel();
                        hasData = false;
                    }
                    resetData();
//                    Toast.makeText(getActivity(), "正在测量中，请稍后...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nskAlgoSdk.NskAlgoStop();

                mSerial.setStop();
            }
        });

        setAlgosButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAlgosButton();
            }
        });

        ecgText.setEnabled(false);
        ecgText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEcg();
            }
        });

        nskAlgoSdk.setOnSignalQualityListener(new NskAlgoSdk.OnSignalQualityListener() {
            @Override
            public void onSignalQuality(int level) {
                final NskAlgoSignalQuality fLevel = new NskAlgoSignalQuality(level);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // change UI elements here
                        String sqStr = "";
                        sqStr += fLevel.toString();
//                        sqText.setText(sqStr);
                        sqTitle.setText("Signal Quality: " + sqStr);
                    }
                });
            }

            @Override
            public void onOverallSignalQuality(final int value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // change UI elements here
                        String sqStr = "overall-";
                        sqStr += Integer.toString(value);
//                        sqText.setText(sqStr);
                        sqTitle.setText("Signal Quality: " + sqStr);
                    }
                });
            }
        });

        nskAlgoSdk.setOnStateChangeListener(new NskAlgoSdk.OnStateChangeListener() {
            @Override
            public void onStateChange(int state, int reason) {
                String stateStr = "";
                String reasonStr = "";
                NskAlgoState s = new NskAlgoState(state);
                stateStr = s.toString();
                NskAlgoState r = new NskAlgoState(reason);
                reasonStr = r.toString();

                Log.d(TAG, "NskAlgoSdkStateChangeListener: state: " + stateStr + ", reason: " + reasonStr);
                final String finalStateStr = stateStr + " | " + reasonStr;
                final int finalState = state;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // change UI elements here
                        stateText.setText(finalStateStr);

                        if (finalState == NskAlgoState.NSK_ALGO_STATE_RUNNING || finalState == NskAlgoState.NSK_ALGO_STATE_COLLECTING_BASELINE_DATA) {
                            bRunning = true;
                            startButton.setText(getActivity().getText(R.string.str_measuring));
                            startButton.setEnabled(true);
                            stopButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_STOP) {
                            bRunning = false;
                            raw_data = null;
                            raw_data_index = 0;
                            startButton.setText(getActivity().getText(R.string.str_measure));
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);

                            output_data_count = 0;
                            output_data = null;

                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_PAUSE) {
                            bRunning = false;
                            startButton.setText(getActivity().getText(R.string.str_measure));
                            startButton.setEnabled(true);
                            stopButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_INITED || finalState == NskAlgoState.NSK_ALGO_STATE_UNINTIED) {
                            bRunning = false;
                            startButton.setText(getActivity().getText(R.string.str_measure));
                            startButton.setEnabled(true);
                            stopButton.setEnabled(false);
                        }
                    }
                });
            }
        });

        nskAlgoSdk.setOnECGHRVFDAlgoIndexListener(new NskAlgoSdk.OnECGHRVFDAlgoIndexListener() {
            @Override
            public void onECGHRVFDAlgoIndexListener(float hf, float lf, float lfhf_ratio, float hflf_ratio) {
                EcgFragment.this.hf = hf;
                EcgFragment.this.lf = lf;
                EcgFragment.this.lfhf = lfhf_ratio;
                EcgFragment.this.hflf = hflf_ratio;
                updateStatus2();
            }
        });

        nskAlgoSdk.setOnECGHRVTDAlgoIndexListener(new NskAlgoSdk.OnECGHRVTDAlgoIndexListener() {
            @Override
            public void onECGHRVTDAlgoIndexListener(float nn50, float sdnn, float pnn50, float rrtranratio, float rmssd) {
                EcgFragment.this.nn50 = nn50;
                EcgFragment.this.sdnn = sdnn;
                EcgFragment.this.pnn50 = pnn50;
                EcgFragment.this.rrtranratio = rrtranratio;
                EcgFragment.this.rmssd = rmssd;
                updateStatus2();
            }
        });

        nskAlgoSdk.setOnECGAlgoIndexListener(new NskAlgoSdk.OnECGAlgoIndexListener() {
            @Override
            public void onECGAlgoIndex(int type, final int value) {

                boolean bStatus = true;

                if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_SMOOTH) {
                    bAddToPlot = true;
                    bStatus = false;

                    if (bAddToPlot) {
                        final String str = "[" + value + "]";
                        final String finalECGStr = str;
                        final float fValue = value;
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // change UI elements here

                                chartData.add(value);

                                AddValueToPlot(ecgSeries, fValue);
                            }
                        });
                    }
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_AFIB) {
                    afib = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_HEARTAGE) {
                    heartage = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_HR) {
                    hr = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_HRV) {
                    hrv = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_MOOD) {
                    mood = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_R2R) {
                    r2r = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_RDETECTED) {
                    rdetected = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_ROBUST_HR) {
                    robust = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_RESPIRATORY_RATE) {
                    resp = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_STRESS) {
                    stress = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_HEARTBEAT) {
                    heartbeat = value;
                } else if (type == NskAlgoECGValueType.NSK_ALGO_ECG_VALUE_TYPE_BASELINE_UPDATED) {
                    // get the baseline and store in local or cloud
                    // please beware this causes the performance issue on the system if commit is invoked too frequent.
                    // it's better to schedule another task to performance this baseline saving task
                    SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor e = settings.edit();
                    byte[] b = nskAlgoSdk.NskAlgoProfileGetBaseline(activeProfile, NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTRATE);

                    String s = Arrays.toString(b);
                    e.putString("ecgbaseline", s);

                    if (e.commit() == false) {
                        Log.d(TAG, "error in saving baseline");
                    }
                }

                if (bStatus) {
                    updateStatus1();
                }
            }
        });

        // initialize our XYPlot reference:
        plot = (XYPlot) viewDelegate.getRootView().findViewById(R.id.myPlot);
        plot.setVisibility(View.INVISIBLE);


        setAlgosButton();

        setEcg();
    }

    private void updateStatus1() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = "afib:" + afib + " heartage:" + heartage + " hrv:" + hrv + "\n" +
                        "rdetected:" + rdetected + " hr:" + hr + " robust:" + robust + "\n" +
                        "mood:" + mood + " stress:" + stress + "\n" +
                        "beat:" + heartbeat + " r2r:" + r2r + " resp:" + resp;
                ecgStatus.setText(str);

                afibTv.setText("afib:" + afib);
                heartageTv.setText("heartage:" + heartage);

                hrvTvs.setText("hrv:" + hrv);
                hrTv.setText("hr:" + hr);
                moodTv.setText("mood:" + mood);
                stressTv.setText("stress:" + stress);
            }
        });
    }

    private void setEcg() {
        removeAllSeriesFromPlot();
        setupPlotWithXRange(-6000, 12000, 1200, "ECG");
//        if (ecgSeries == null) {
        ecgSeries = createSeries("ECG");
//        }
        addSeries(plot, ecgSeries, R.xml.line_point_formatter_with_plf1);
        plot.redraw();
    }


    private void setAlgosButton() {
        // check selected algos
        Log.i(TAG, "Set Algos");

        startButton.setEnabled(false);
        stopButton.setEnabled(false);
        clearAllSeries();

        stateText.setText("");
        sqTitle.setText("Signal Quality: NONE");

        currentSelectedAlgo = 0;
        if (ecgHRVFD.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HRVFD;
        if (ecgHRVTD.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HRVTD;
        if (ecgHRV.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HRV;
        if (ecgSmooth.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_SMOOTH;
        if (ecgAfib.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_AFIB;
        if (ecgHeartage.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTAGE;
        if (ecgHeartrate.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTRATE;
        if (ecgMood.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_MOOD;
        if (ecgResp.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_RESPIRATORY;
        if (ecgStress.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_STRESS;

        if (currentSelectedAlgo == 0) {
            showDialog("Please select at least one algorithm");
        } else {
            String path = getActivity().getFilesDir().getAbsolutePath();

            if (bInited) {
                nskAlgoSdk.NskAlgoUninit();
                bInited = false;
                bWaitQ = false;
                bSendQ = false;
            }

            int ret = nskAlgoSdk.NskAlgoInit(currentSelectedAlgo, path, license);
            if (ret == 0) {
                bInited = true;
                showToast("Algo SDK has been initialized successfully", Toast.LENGTH_LONG);
            } else {
                bInited = false;
                showToast("Failed to initialize the SDK, code = " + String.valueOf(ret), Toast.LENGTH_LONG);
                return;
            }

            boolean b = nskAlgoSdk.setBaudRate(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG, NskAlgoSampleRate.NSK_ALGO_SAMPLE_RATE_512);
            if (b != true) {
                showToast("Failed to set the sampling rate", Toast.LENGTH_LONG);
                return;
            }

            ecgText.setEnabled(true);
            ecgSeries = createSeries("ECG");

            // init the active profile here
            NskAlgoProfile[] profiles = nskAlgoSdk.NskAlgoProfiles();

            // do some test, clear all the profiles
            int i = 0;
            for (i = 0; i < profiles.length; ++i) {
                nskAlgoSdk.NskAlgoProfileDelete(profiles[0].userId);
            }
            profiles = nskAlgoSdk.NskAlgoProfiles();

            if (profiles.length == 0) {
                // create a default profile
                try {
                    String dobStr = "2019-9-12";
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                    Date dob = df.parse(dobStr);

                    NskAlgoProfile profile = new NskAlgoProfile();
                    profile.name = "abc";
                    profile.height = 180;
                    profile.weight = 90;
                    profile.gender = true;
                    profile.dob = dob;
                    Log.i(TAG, "profile : " + new Gson().toJson(profile));
                    if (nskAlgoSdk.NskAlgoProfileUpdate(profile) == false) {
                        Log.d(TAG, "fail to setup the profile");
                    }

                    profiles = nskAlgoSdk.NskAlgoProfiles();
                    Log.i(TAG, "NskAlgoProfiles profile : " + new Gson().toJson(profile));
                    // setup the ECG config
                    if (ecgAfib.isEnabled())
                        assert (nskAlgoSdk.NskAlgoSetECGConfigAfib((float) 3.5) == true);
                    if (ecgStress.isEnabled())
                        assert (nskAlgoSdk.NskAlgoSetECGConfigStress(30, 30) == true);
                    if (ecgHeartage.isEnabled())
                        assert (nskAlgoSdk.NskAlgoSetECGConfigHeartage(30) == true);
                    if (ecgHRV.isEnabled())
                        assert (nskAlgoSdk.NskAlgoSetECGConfigHRV(30) == true);
                    if (ecgHRVTD.isEnabled())
                        assert (nskAlgoSdk.NskAlgoSetECGConfigHRVTD(30, 30) == true);
                    if (ecgHRVFD.isEnabled())
                        assert (nskAlgoSdk.NskAlgoSetECGConfigHRVFD(30, 30) == true);

                    // nskAlgoSdk.setSignalQualityWatchDog((short)20, (short)5);

                    // retrieve the baseline data
                    if (profiles.length > 0) {
                        activeProfile = profiles[0].userId;
                        nskAlgoSdk.NskAlgoProfileActive(activeProfile);

                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getActivity());
                        String stringArray = settings.getString("ecgbaseline", null);
                        if (stringArray != null) {
                            String[] split = stringArray.substring(1, stringArray.length() - 1).split(", ");
                            byte[] array = new byte[split.length];
                            for (i = 0; i < split.length; ++i) {
                                array[i] = Byte.parseByte(split[i]);
                            }
                            if (nskAlgoSdk.NskAlgoProfileSetBaseline(activeProfile, NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTRATE, array) != true) {
                                Log.d(TAG, "error in setting the profile baseline");
                            }
                        }
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            Log.d(TAG, "NSK_ALGO_Init() " + ret);
            String sdkVersion = "SDK ver.: " + nskAlgoSdk.NskAlgoSdkVersion();
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTRATE) != 0) {
                sdkVersion += "\nHeartrate ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTRATE);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTAGE) != 0) {
                sdkVersion += "\nHeartage ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTAGE);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_AFIB) != 0) {
                sdkVersion += "\nAfib ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_AFIB);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_HRV) != 0) {
                sdkVersion += "\nHRV ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_HRV);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_MOOD) != 0) {
                sdkVersion += "\nMood ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_MOOD);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_RESPIRATORY) != 0) {
                sdkVersion += "\nResp ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_RESPIRATORY);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_SMOOTH) != 0) {
                sdkVersion += "\nSmooth ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_SMOOTH);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_STRESS) != 0) {
                sdkVersion += "\nStress ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_STRESS);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_HRVFD) != 0) {
                sdkVersion += "\nHRVFD ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_HRVFD);
            }
            if ((currentSelectedAlgo & NskAlgoType.NSK_ALGO_TYPE_ECG_HRVFD) != 0) {
                sdkVersion += "\nHRVTD ver.: " + nskAlgoSdk.NskAlgoAlgoVersion(NskAlgoType.NSK_ALGO_TYPE_ECG_HRVFD);
            }

            showToast(sdkVersion, Toast.LENGTH_LONG);
        }
    }

    private float hf, lf, lfhf, hflf, nn50, sdnn, pnn50, rrtranratio, rmssd;

    private void updateStatus2() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = String.format("hf lf ratio [%.2f,%.2f,%.2f,%.2f]\nnn50 sdnn [%.2f,%.2f,%.2f]\npnn50 rrratio [%.2f,%.2f]",
                        hf, lf, lfhf, hflf, nn50, sdnn, pnn50, rrtranratio, rmssd);
                ecgStatus2.setText(str);

                hfLfTv.setText(String.format("hf lf ratio [%.2f,%.2f,%.2f,%.2f]", hf, lf, lfhf, hflf));

                pnnTv.setText(String.format("pnn50 rrratio [%.2f,%.2f]", rrtranratio, rmssd));

                sdnnTv.setText(String.format("nn50 sdnn [%.2f,%.2f,%.2f]", nn50, sdnn, pnn50));

                viewDelegate.getRootView().findViewById(R.id.status_view).setVisibility(View.VISIBLE);
            }
        });
    }

    private void resetData() {
        hf = 0f;
        lf = 0f;
        lfhf = 0f;
        hflf = 0f;
        nn50 = 0f;
        sdnn = 0f;
        pnn50 = 0f;
        rrtranratio = 0f;
        rmssd = 0f;
        updateStatus2();
        hr = 0;
        robust = -1;
        mood = 0;
        r2r = -1;
        hrv = 0;
        heartage = -1;
        afib = -1;
        rdetected = -1;
        stress = -1;
        heartbeat = -1;
        resp = -1;
        updateStatus1();
    }

    private void enableCheckBox(String name) {
        CheckBox m = null;

        if (name.equalsIgnoreCase("hr")) {
            m = ecgHeartrate;
        } else if (name.equalsIgnoreCase("sm")) {
            m = ecgSmooth;
        } else if (name.equalsIgnoreCase("af")) {
            m = ecgAfib;
        } else if (name.equalsIgnoreCase("hv")) {
            m = ecgHRV;
        } else if (name.equalsIgnoreCase("md")) {
            m = ecgMood;
        } else if (name.equalsIgnoreCase("rr")) {
            m = ecgResp;
        } else if (name.equalsIgnoreCase("ha")) {
            m = ecgHeartage;
        } else if (name.equalsIgnoreCase("st")) {
            m = ecgStress;
        } else if (name.equalsIgnoreCase("stv2")) {
            m = ecgStress;
        } else if (name.equalsIgnoreCase("td")) {
            m = ecgHRVTD;
        } else if (name.equalsIgnoreCase("fd")) {
            m = ecgHRVFD;
        }

        if (m != null) {
            m.setEnabled(true);
            m.setChecked(true);
        }
    }

    private void loadsetup() {
        AssetManager assetManager = getActivity().getAssets();
        InputStream inputStream = null;

        try {
            String prefix = "license key=\"";
            String suffix = "\"";
            String pattern = prefix + "(.+?)" + suffix;
            Pattern p = Pattern.compile(pattern);

            inputStream = assetManager.open("license.txt");
            ArrayList<String> data = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                while (true) {
                    String line = reader.readLine();
                    if (line == null || line.isEmpty())
                        break;
                    Matcher m = p.matcher(line);
                    if (m.find()) {
                        license = line.substring(m.regionStart() + prefix.length(), m.regionEnd() - suffix.length());
                        break;
                    }
                }
            } catch (IOException e) {

            }
            inputStream.close();
        } catch (IOException e) {
            Log.e(TAG, "Cant load the license file");
        }

        try {
            inputStream = assetManager.open("setupinfo.txt");
            ArrayList<String> data = new ArrayList<String>();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            try {
                while (true) {
                    String line = reader.readLine();
                    if (line == null || line.isEmpty()) {
                        break;
                    }
                    data.add(line);
                }
            } catch (IOException e) {

            }
            inputStream.close();

            for (int i = 0; i < data.size(); ++i) {
                enableCheckBox(data.get(i));
            }

        } catch (IOException e) {
            Log.e(TAG, "Cant load the setup file");
        }

        Log.d(TAG, "Finished reading data");
    }


    int raw_index = 0;

    int data_type = 0;

    boolean heart = false;

    SerialPortUtil.OnDataReceiveListener serialReceiveListener = new SerialPortUtil.OnDataReceiveListener() {

        @Override
        public void onDataReceive(short[] buffer) {
            if (bInited) {

                if (!bSendQ) {
                    Log.i(TAG, "onDataReceive NskAlgoQueryOverallQuality");
                    bSendQ = true;
                    raw_index = 0;
                    nskAlgoSdk.NskAlgoQueryOverallQuality(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG);
                    short pqValue[] = {(short) 200};
                    nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG_PQ, pqValue, 1);
                    return;
                }

                if (data_type == 200) {
                    if (heart) {
                        heart = false;
                        short pqValue[] = {(short) 200};
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG_PQ, pqValue, 1);
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG_PQ");
                    } else {
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG, buffer, 1);
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG");
                    }

/*                    if (raw_index == 0 || raw_index % 200 == 0) {
                        short pqValue[] = {(short) 200};
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG_PQ, pqValue, 1);
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG_PQ");
                    } else {
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG, buffer, 1);
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG");
                    }
                    raw_index++;*/
                }
            }

        }

        @Override
        public void onDataType(int type) {
            data_type = type;
            heart = true;
        }
    };

    boolean bAddToPlot = false;

    private void removeAllSeriesFromPlot() {
        if (ecgSeries != null) {
            plot.removeSeries(ecgSeries);
        }
    }

    private void clearAllSeries() {
        if (ecgSeries != null) {
            plot.removeSeries(ecgSeries);
            ecgSeries = null;
        }
        plot.setVisibility(View.INVISIBLE);
    }

    private XYPlot setupPlotWithXRange(Number yMin, Number yMax, Number xMax, String title) {
        // initialize our XYPlot reference:
        mCurrentXRange = xMax.intValue();
        plot = (XYPlot) viewDelegate.getRootView().findViewById(R.id.myPlot);

        plot.setDomainBoundaries(0, xMax, BoundaryMode.FIXED);

        if ((yMax.intValue() - yMin.intValue()) < 10) {
            plot.setRangeStepValue((yMax.intValue() - yMin.intValue() + 1));
        } else {
            plot.setRangeStepValue(11);
        }
        plot.setRangeBoundaries(yMin.intValue(), yMax.intValue(), BoundaryMode.FIXED);

        //plot.getGraphWidget().getGridBackgroundPaint().setColor(Color.WHITE);
        plot.getGraph().getGridBackgroundPaint().setColor(Color.WHITE);

        plot.setPlotPadding(0, 0, 0, 0);
        plot.setTitle(title);

        //Remove legend
        plot.getLayoutManager().remove(plot.getLegend());
        plot.getLayoutManager().remove(plot.getDomainTitle());
        plot.getLayoutManager().remove(plot.getRangeTitle());
        plot.getLayoutManager().remove(plot.getTitle());

        plot.setVisibility(View.VISIBLE);

        return plot;
    }

    private XYPlot setupPlot(Number rangeMin, Number rangeMax, String title) {
        // initialize our XYPlot reference:
        return setupPlotWithXRange(rangeMin, rangeMax, X_RANGE, title);
    }

    private SimpleXYSeries createSeries(String seriesName) {
        // Turn the above arrays into XYSeries':
        SimpleXYSeries series = new SimpleXYSeries(
                null,          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                null);                             // Set the display title of the series
        series.useImplicitXVals();
        return series;
    }

    private SimpleXYSeries addSeries(XYPlot plot, SimpleXYSeries series, int formatterId) {
        // Create a formatter to use for drawing a series using LineAndPointRenderer
        // and configure it from xml:
        LineAndPointFormatter seriesFormat = new LineAndPointFormatter();

//        LineAndPointFormatter seriesFormat = new LineAndPointFormatter(Color.argb(0xff,0x22,0xaa,0xff),
//                Color.GREEN, null ,null);

        Paint lineFill = new Paint();
        lineFill.setAlpha(200);
        lineFill.setShader(new LinearGradient(0, 0, 0, 500, Color.argb(0xff, 0xff, 0xff, 0xff),
                Color.argb(0xff, 0xaa, 0xcc, 0xff), Shader.TileMode.MIRROR));
        seriesFormat.setFillPaint(lineFill);

        seriesFormat.setPointLabelFormatter(null);
        seriesFormat.configure(getActivity(), formatterId);
        seriesFormat.setVertexPaint(null);
        series.useImplicitXVals();

        plot.addSeries(series, seriesFormat);
        return series;
    }

    private void AddValueToPlot(SimpleXYSeries series, float value) {
        if (series.size() >= mCurrentXRange) {
            series.removeFirst();
        }
        Number num = value;
        series.addLast(null, num);
        plot.redraw();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (bRunning) {
            nskAlgoSdk.NskAlgoStop();
            nskAlgoSdk.NskAlgoUninit();
            mSerial.setStop();
    //        }

            if (cdTimer != null) {
                cdTimer.cancel();
                cdTimer = null;
            }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void showToast(final String msg, final int timeStyle) {
        Log.i(TAG, "showToast msg : " + msg);
        /*getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), msg, timeStyle).show();
            }

        });*/
    }

    private void showDialog(String message) {
        new AlertDialog.Builder(getActivity())
                .setTitle("")
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    public void onClick(View view) {

    }

}
