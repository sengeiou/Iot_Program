package com.vtech.check.fragment;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.neurosky.AlgoSdk.NskAlgoDataType;
import com.neurosky.AlgoSdk.NskAlgoECGValueType;
import com.neurosky.AlgoSdk.NskAlgoProfile;
import com.neurosky.AlgoSdk.NskAlgoSampleRate;
import com.neurosky.AlgoSdk.NskAlgoSdk;
import com.neurosky.AlgoSdk.NskAlgoSignalQuality;
import com.neurosky.AlgoSdk.NskAlgoState;
import com.neurosky.AlgoSdk.NskAlgoType;
import com.vtech.check.R;
import com.vtech.check.serfragment.ecg.EcgDelegate;
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

public class ECGTest extends BaseTest{

    View rootview;

    final String TAG = "ECGTestTAG";
    SerialPortUtil mSerial;

    // graph plot variables
    private final static int X_RANGE = 50;
    private int mCurrentXRange = X_RANGE;
//    private SimpleXYSeries ecgSeries = null;

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
//    private XYPlot plot;
    private Button setAlgosButton;
    private Button startButton;
    private Button stopButton;
    private Button ecgText;
    private CheckBox ecgHeartrate;
//    private CheckBox ecgStress;
//    private CheckBox ecgSmooth;
//    private CheckBox ecgHRV;
//    private CheckBox ecgAfib;
//    private CheckBox ecgMood;
//    private CheckBox ecgHeartage;
//    private CheckBox ecgResp;
//    private CheckBox ecgHRVTD;
//    private CheckBox ecgHRVFD;
//    private TextView stateText;
//    private TextView sqTitle;
//    private TextView ecgStatus;
//    private TextView ecgStatus2;


//    TextView hfLfTv;
//
//    TextView pnnTv;
//
//    TextView sdnnTv;
//
//    TextView afibTv;
//
//    TextView hrvTvs;
//
//    TextView moodTv;
//
//    TextView heartageTv;
//
//    TextView hrTv;
//
//    TextView stressTv;

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

    boolean hasData = false;

    List<Integer> chartData = new ArrayList<>();

    boolean bAddToPlot = false;

    TextView ecg_text;

    StringBuffer strtxt = new StringBuffer("");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootview==null){
            rootview=inflater.inflate(R.layout.ecg,container,false);
        }
       ecg_text=rootview.findViewById(R.id.ecg_text);
       initViewData();
        return rootview;
    }




    private void initView() {
        startButton = rootview.findViewById(R.id.startButton);
        stopButton = rootview.findViewById(R.id.stopButton);
        setAlgosButton = rootview.findViewById(R.id.setAlgosButton);
        ecgText = rootview.findViewById(R.id.ecgGraph);


        ecgHeartrate = rootview.findViewById(R.id.ecgHeartrate);
//        ecgAfib = rootview.findViewById(R.id.ecgAfib);
//        ecgHeartage = rootview.findViewById(R.id.ecgHeartage);
//        ecgHRV = rootview.findViewById(R.id.ecgHRV);
//        ecgMood = rootview.findViewById(R.id.ecgMood);
//        ecgResp = rootview.findViewById(R.id.ecgResp);
//        ecgStress = rootview.findViewById(R.id.ecgStress);
//        ecgSmooth = rootview.findViewById(R.id.ecgSmooth);
//        ecgHRVTD = rootview.findViewById(R.id.ecgHRVTD);
//        ecgHRVFD = rootview.findViewById(R.id.ecgHRVFD);
//
//        stateText = (TextView) rootview.findViewById(R.id.stateText);
//        sqTitle = (TextView) rootview.findViewById(R.id.sqTitle);


//        hfLfTv = (TextView) rootview.findViewById(R.id.hf_lf_tv);
//        pnnTv = (TextView) rootview.findViewById(R.id.pnn_tv);
//        sdnnTv = (TextView) rootview.findViewById(R.id.sdnn_tv);
//
//        afibTv = (TextView)  rootview.findViewById(R.id.afib_tv);
//        hrvTvs = (TextView)  rootview.findViewById(R.id.hrv_tvs);
//        moodTv = (TextView)  rootview.findViewById(R.id.mood_tv);
//        heartageTv = (TextView)  rootview.findViewById(R.id.heartage_tv);
//        hrTv = (TextView)  rootview.findViewById(R.id.hr_tv);
//        stressTv = (TextView)  rootview.findViewById(R.id.stress_tv);
//
//        ecgStatus = (TextView) rootview.findViewById(R.id.ecgStatus);
//        ecgStatus2 = (TextView) rootview.findViewById(R.id.ecgStatus2);

//        viewDelegate.getRootView().findViewById(R.id.question1).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new EcgHrvAlertDialog(getActivity(), R.style.customDialog)
////                        .setTitle(message)
//                        .setPositiveButton(getActivity().getString(R.string.str_sure), new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        }).showDialog();
//            }
//        });
//
//        viewDelegate.getRootView().findViewById(R.id.question2).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new EcgAlertDialog(getActivity(), R.style.customDialog)
////                        .setTitle(message)
//                        .setPositiveButton(getActivity().getString(R.string.str_sure), new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//
//                            }
//                        }).showDialog();
//            }
//        });

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

    private void enableCheckBox(String name) {
        CheckBox m = null;

        if (name.equalsIgnoreCase("hr")) {
            m = ecgHeartrate;
        }
//        else if (name.equalsIgnoreCase("sm")) {
//            m = ecgSmooth;
//        } else if (name.equalsIgnoreCase("af")) {
//            m = ecgAfib;
//        } else if (name.equalsIgnoreCase("hv")) {
//            m = ecgHRV;
//        } else if (name.equalsIgnoreCase("md")) {
//            m = ecgMood;
//        } else if (name.equalsIgnoreCase("rr")) {
//            m = ecgResp;
//        } else if (name.equalsIgnoreCase("ha")) {
//            m = ecgHeartage;
//        } else if (name.equalsIgnoreCase("st")) {
//            m = ecgStress;
//        } else if (name.equalsIgnoreCase("stv2")) {
//            m = ecgStress;
//        } else if (name.equalsIgnoreCase("td")) {
//            m = ecgHRVTD;
//        } else if (name.equalsIgnoreCase("fd")) {
//            m = ecgHRVFD;
//        }

        if (m != null) {
            m.setEnabled(true);
            m.setChecked(true);
        }
    }

    private float hf, lf, lfhf, hflf, nn50, sdnn, pnn50, rrtranratio, rmssd;
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

    private void updateStatus1() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = "afib:" + afib + " heartage:" + heartage + " hrv:" + hrv + "\n" +
                        "rdetected:" + rdetected + " hr:" + hr + " robust:" + robust + "\n" +
                        "mood:" + mood + " stress:" + stress + "\n" +
                        "beat:" + heartbeat + " r2r:" + r2r + " resp:" + resp;
//                ecgStatus.setText(str);
//
//                afibTv.setText("afib:" + afib);
//                heartageTv.setText("heartage:" + heartage);
//
//                hrvTvs.setText("hrv:" + hrv);
//                hrTv.setText("hr:" + hr);
//                moodTv.setText("mood:" + mood);
//                stressTv.setText("stress:" + stress);
                setStringBuffer(str);
            }
        });
    }


    private void updateStatus2() {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String str = String.format("hf lf ratio [%.2f,%.2f,%.2f,%.2f]\nnn50 sdnn [%.2f,%.2f,%.2f]\npnn50 rrratio [%.2f,%.2f]",
                        hf, lf, lfhf, hflf, nn50, sdnn, pnn50, rrtranratio, rmssd);
//                ecgStatus2.setText(str);
//
//                hfLfTv.setText(String.format("hf lf ratio [%.2f,%.2f,%.2f,%.2f]", hf, lf, lfhf, hflf));
//
//                pnnTv.setText(String.format("pnn50 rrratio [%.2f,%.2f]", rrtranratio, rmssd));
//
//                sdnnTv.setText(String.format("nn50 sdnn [%.2f,%.2f,%.2f]", nn50, sdnn, pnn50));

                rootview.findViewById(R.id.status_view).setVisibility(View.VISIBLE);


                setStringBuffer(String.format("hf lf ratio [%.2f,%.2f,%.2f,%.2f]", hf, lf, lfhf, hflf));
                setStringBuffer(String.format("pnn50 rrratio [%.2f,%.2f]", rrtranratio, rmssd));
                setStringBuffer(String.format("nn50 sdnn [%.2f,%.2f,%.2f]", nn50, sdnn, pnn50));
//                ecg_text.append();
//                ecg_text.append(String.format("pnn50 rrratio [%.2f,%.2f]", rrtranratio, rmssd));
//                ecg_text.append(String.format("nn50 sdnn [%.2f,%.2f,%.2f]", nn50, sdnn, pnn50));
            }
        });
    }


    private void initViewData() {

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
//                    player("请放好手指正在采集数据");

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
//                        sqTitle.setText("Signal Quality: " + sqStr);
//                        ecg_text.append(sqStr);
                        setStringBuffer(sqStr);
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
//                        sqTitle.setText("Signal Quality: " + sqStr);

                        setStringBuffer(String.valueOf(value));
//                        ecg_text.append(String.valueOf(value));
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

//                ecg_text.append(reasonStr);
                setStringBuffer(reasonStr);

                Log.d(TAG, "NskAlgoSdkStateChangeListener: state: " + stateStr + ", reason: " + reasonStr);
                final String finalStateStr = stateStr + " | " + reasonStr;
                final int finalState = state;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // change UI elements here
//                        stateText.setText(finalStateStr);

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
                ECGTest.this.hf = hf;
                ECGTest.this.lf = lf;
                ECGTest.this.lfhf = lfhf_ratio;
                ECGTest.this.hflf = hflf_ratio;
                updateStatus2();
            }
        });

        nskAlgoSdk.setOnECGHRVTDAlgoIndexListener(new NskAlgoSdk.OnECGHRVTDAlgoIndexListener() {
            @Override
            public void onECGHRVTDAlgoIndexListener(float nn50, float sdnn, float pnn50, float rrtranratio, float rmssd) {
                ECGTest.this.nn50 = nn50;
                ECGTest.this.sdnn = sdnn;
                ECGTest.this.pnn50 = pnn50;
                ECGTest.this.rrtranratio = rrtranratio;
                ECGTest.this.rmssd = rmssd;
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

//                                AddValueToPlot(ecgSeries, fValue);
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
//        plot = (XYPlot) viewDelegate.getRootView().findViewById(R.id.myPlot);
//        plot.setVisibility(View.INVISIBLE);


        setAlgosButton();

        setEcg();
    }












    private void setEcg() {
//        removeAllSeriesFromPlot();
//        setupPlotWithXRange(-6000, 12000, 1200, "ECG");
////        if (ecgSeries == null) {
//        ecgSeries = createSeries("ECG");
////        }
//        addSeries(plot, ecgSeries, R.xml.line_point_formatter_with_plf1);
//        plot.redraw();
    }


    private void setAlgosButton() {
        // check selected algos
        Log.i(TAG, "Set Algos");

        startButton.setEnabled(false);
        stopButton.setEnabled(false);
//        clearAllSeries();

//        stateText.setText("");
//        sqTitle.setText("Signal Quality: NONE");

        currentSelectedAlgo = 0;
//        if (ecgHRVFD.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HRVFD;
//        if (ecgHRVTD.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HRVTD;
//        if (ecgHRV.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HRV;
//        if (ecgSmooth.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_SMOOTH;
//        if (ecgAfib.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_AFIB;
//        if (ecgHeartage.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTAGE;
        if (ecgHeartrate.isChecked())
            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_HEARTRATE;
//        if (ecgMood.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_MOOD;
//        if (ecgResp.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_RESPIRATORY;
//        if (ecgStress.isChecked())
//            currentSelectedAlgo |= NskAlgoType.NSK_ALGO_TYPE_ECG_STRESS;

        if (currentSelectedAlgo == 0) {
            showToast("Please select at least one algorithm",0);
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
//            ecgSeries = createSeries("ECG");

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
//                    if (ecgAfib.isEnabled())
//                        assert (nskAlgoSdk.NskAlgoSetECGConfigAfib((float) 3.5) == true);
//                    if (ecgStress.isEnabled())
//                        assert (nskAlgoSdk.NskAlgoSetECGConfigStress(30, 30) == true);
//                    if (ecgHeartage.isEnabled())
//                        assert (nskAlgoSdk.NskAlgoSetECGConfigHeartage(30) == true);
//                    if (ecgHRV.isEnabled())
//                        assert (nskAlgoSdk.NskAlgoSetECGConfigHRV(30) == true);
//                    if (ecgHRVTD.isEnabled())
//                        assert (nskAlgoSdk.NskAlgoSetECGConfigHRVTD(30, 30) == true);
//                    if (ecgHRVFD.isEnabled())
//                        assert (nskAlgoSdk.NskAlgoSetECGConfigHRVFD(30, 30) == true);

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



    public void showToast(final String msg, final int timeStyle) {
        Log.i(TAG, "showToast msg : " + msg);
        /*getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), msg, timeStyle).show();
            }

        });*/
    }

    int raw_index = 0;

    int data_type = 0;

    boolean heart = false;
    SerialPortUtil.OnDataReceiveListener serialReceiveListener = new SerialPortUtil.OnDataReceiveListener() {

        @Override
        public void onDataReceive(short[] buffer) {
            if (bInited) {

                  setStringBuffer(String.valueOf(buffer));
//                ecg_text.append(String.valueOf(buffer));
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
//                player(getActivity().getString(R.string.ecg_measurement_completed));
                hasData = false;
                if (chartData.size() > 0 && chartData.size() > 1000) {
                    chartData = chartData.subList(chartData.size() - 1000, chartData.size());

                    String data = new Gson().toJson(chartData);
//                    ecg_text.append(data);
                    setStringBuffer(data);
                    Log.e(TAG, "chartData size == " + chartData.size() + " , chardata is " + data);
//                    EcgBean bean = new EcgBean();
//                    bean.setEcgData(data);
//                    bean.save();


//                    sendChartBroadcast(data);
//                    uploadToServer(data);


                }
            }
        }
    };








    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode==KeyEvent.KEYCODE_BACK){
            nskAlgoSdk.NskAlgoStop();
            nskAlgoSdk.NskAlgoUninit();
            mSerial.setStop();
            //        }

            if (cdTimer != null) {
                cdTimer.cancel();
                cdTimer = null;
            }
        }
        return super.onKeyDown(keyCode, event);

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
    public String getTestName() {
        return getContext().getString(R.string.ecg_test);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("ecg",true);
    }


    public synchronized void setStringBuffer(String stringBuffer){
//        strtxt.append(stringBuffer);
//        ecg_text.setText(strtxt);
    }

}
