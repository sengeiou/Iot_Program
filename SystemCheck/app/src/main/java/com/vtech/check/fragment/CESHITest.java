package com.vtech.check.fragment;

import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
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
import com.neurosky.AlgoSdk.NskAlgoState;
import com.neurosky.AlgoSdk.NskAlgoType;
import com.vtech.check.R;
import com.vtech.check.serial.SerialPortUtil;
import com.vtech.check.utils.ToastUtil;

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

public class CESHITest extends BaseTest{

    final String TAG = "CESHITest";
    View rootview;
    TextView ecg_xs_text;
    Button startButton;

    private boolean bRunning = false;
    /**
     * ECG SDK Release license
     */
    private String license = "";
    /**
     * The ECG SDK instance
     */
    private NskAlgoSdk nskAlgoSdk;
    SerialPortUtil mSerial;
    List<Integer> chartData = new ArrayList<>();


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

    private int currentSelectedAlgo;
    private int activeProfile = -1;

    int data_type = 0;

    String ecgtext="";

    private static final int COMPLETED = 1000;

    private MyCountdownTimer countdowntimer;
    private MyCountdownTimer1 countdowntimer1;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
//        if (rootview==null){
            rootview=inflater.inflate(R.layout.fragment_ceshi,container,false);
//        }
        initView();
        loadsetup();
        return rootview;
    }

    boolean hasData = false;

    private void initView() {

        nskAlgoSdk = new NskAlgoSdk();
        startButton=rootview.findViewById(R.id.startButtonCeShi);
        ecg_xs_text=rootview.findViewById(R.id.ecg_xs_text);
        ecg_xs_text.setMovementMethod(ScrollingMovementMethod.getInstance());

        ecgHeartrate = (CheckBox) rootview.findViewById(R.id.ecgHeartrate);
        ecgAfib = (CheckBox) rootview.findViewById(R.id.ecgAfib);
        ecgHeartage = (CheckBox) rootview.findViewById(R.id.ecgHeartage);
        ecgHRV = (CheckBox) rootview.findViewById(R.id.ecgHRV);
        ecgMood = (CheckBox) rootview.findViewById(R.id.ecgMood);
        ecgResp = (CheckBox) rootview.findViewById(R.id.ecgResp);
        ecgStress = (CheckBox) rootview.findViewById(R.id.ecgStress);
        ecgSmooth = (CheckBox) rootview.findViewById(R.id.ecgSmooth);
        ecgHRVTD = (CheckBox) rootview.findViewById(R.id.ecgHRVTD);
        ecgHRVFD = (CheckBox) rootview.findViewById(R.id.ecgHRVFD);


        mSerial = new SerialPortUtil(SerialPortUtil.RESPIRE_DEV_PATH);

        mSerial.setOnDataReceiveListener(serialReceiveListener);

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  ecg_xs_text.setText("测量中");
                  startButton.setText(getActivity().getText(R.string.str_measuring));
                  startButton.setEnabled(false);
//                if (!bRunning) {
                    nskAlgoSdk.NskAlgoStart(false);
                    mSerial.onCreate();
                    mSerial.setStart();
                  //  player("请放好手指正在采集数据");
//                    cdTimer.start();
                    if (countdowntimer1!=null){
                        countdowntimer1.start();
                    }else {
                        countdowntimer1=new MyCountdownTimer1(60*1000,1000);
                        countdowntimer1.start();
                    }


            }
        });

        nskAlgoSdk.setOnECGAlgoIndexListener(new NskAlgoSdk.OnECGAlgoIndexListener() {
            @Override
            public void onECGAlgoIndex(final int type, final int value) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // change UI elements here
                        Log.i(TAG, "OnECGAlgoIndexListener"+type);
                        Log.i(TAG, "OnECGAlgoIndexListener"+value);

//                       ecg_xs_text.append(String.valueOf(type)+"");
//                       ecg_xs_text.append(String.valueOf(value)+"");
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

                        if (finalState == NskAlgoState.NSK_ALGO_STATE_RUNNING || finalState == NskAlgoState.NSK_ALGO_STATE_COLLECTING_BASELINE_DATA) {
                            bRunning = true;
                            startButton.setText(getActivity().getText(R.string.str_measuring));
                            startButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_STOP) {
                            bRunning = false;
//                            startButton.setText(getActivity().getText(R.string.str_measure));
                            startButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_PAUSE) {
                            bRunning = false;
//                            startButton.setText(getActivity().getText(R.string.str_measure));
                            startButton.setEnabled(true);
                        } else if (finalState == NskAlgoState.NSK_ALGO_STATE_INITED || finalState == NskAlgoState.NSK_ALGO_STATE_UNINTIED) {
                            bRunning = false;
//                            startButton.setText(getActivity().getText(R.string.str_measure));
                            startButton.setEnabled(true);
                        }
                    }
                });
            }
        });




        setAlgosButton();


        if (countdowntimer!=null){
            countdowntimer.start();
        }else {
            countdowntimer = new MyCountdownTimer(60*1000, 1000);
            countdowntimer.start();
        }


    }





    protected class MyCountdownTimer extends CountDownTimer {


        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountdownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            ecg_xs_text.setText(ecgtext);
        }

        @Override
        public void onFinish() {
            ecg_xs_text.setText(ecgtext);
        }
    }



    protected class MyCountdownTimer1 extends CountDownTimer {


        /**
         * @param millisInFuture    The number of millis in the future from the call
         *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
         *                          is called.
         * @param countDownInterval The interval along the way to receive
         *                          {@link #onTick(long)} callbacks.
         */
        public MyCountdownTimer1(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished ==  59 * 1000) {
                nskAlgoSdk.NskAlgoQueryOverallQuality(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG);
            }
        }

        @Override
        public void onFinish() {
            if (bRunning) {
                nskAlgoSdk.NskAlgoStop();
                mSerial.setStop();
                /* 测量已经结束 */
//                player(getActivity().getString(R.string.ecg_measurement_completed));
                hasData = false;
                if (chartData.size() > 0 && chartData.size() > 1000) {
                    chartData = chartData.subList(chartData.size() - 1000, chartData.size());

                    String data = new Gson().toJson(chartData);
                    Log.e(TAG, "chartData size == " + chartData.size() + " , chardata is " + data);

                }
            }
        }
    }









    private CountDownTimer cdTimer = new CountDownTimer( 60 * 1000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            if (millisUntilFinished ==  59 * 1000) {
                nskAlgoSdk.NskAlgoQueryOverallQuality(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG);
//                ecg_xs_text.append("onTick");
                Log.i(TAG, "startSendECGThread NskAlgoQueryOverallQuality");
            }
        }

        @Override
        public void onFinish() {
            if (bRunning) {
                nskAlgoSdk.NskAlgoStop();
                mSerial.setStop();
                /* 测量已经结束 */
//                player(getActivity().getString(R.string.ecg_measurement_completed));
                hasData = false;
                if (chartData.size() > 0 && chartData.size() > 1000) {
                    chartData = chartData.subList(chartData.size() - 1000, chartData.size());

                    String data = new Gson().toJson(chartData);
                    Log.e(TAG, "chartData size == " + chartData.size() + " , chardata is " + data);

                }
            }
        }
    };







    private void setAlgosButton() {
        // check selected algos
        Log.i(TAG, "Set Algos");

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
//            showDialog("Please select at least one algorithm");
        } else {
            String path = getActivity().getFilesDir().getAbsolutePath();


            int ret = nskAlgoSdk.NskAlgoInit(currentSelectedAlgo, path, license);

            boolean b = nskAlgoSdk.setBaudRate(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG, NskAlgoSampleRate.NSK_ALGO_SAMPLE_RATE_512);
            if (b != true) {
//                showToast("Failed to set the sampling rate", Toast.LENGTH_LONG);
                return;
            }

//            ecgText.setEnabled(true);
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

//            showToast(sdkVersion, Toast.LENGTH_LONG);
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



    SerialPortUtil.OnDataReceiveListener serialReceiveListener = new SerialPortUtil.OnDataReceiveListener() {

        @Override
        public void onDataReceive(short[] buffer) {


            Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG_PQ     "+buffer[0]);

                if (data_type == 200) {

                   /* if (heart) {
                        heart = false;
                        short pqValue[] = {(short) 200};
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG_PQ, pqValue, 1);*/
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG_PQ  123456  "+buffer[0]);
//                       ecg_xs_text.append(""+buffer[0]);

                        ecgtext=ecgtext+buffer[0];


//                                        ToastUtil.show(getContext(),"手指已经放置");
                /*    } else {
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG, buffer, 1);
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG");
                    }*/
                }
             /* if (raw_index == 0 || raw_index % 200 == 0) {
                        short pqValue[] = {(short) 200};
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG_PQ, pqValue, 1);
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG_PQ");
                    } else {
                        nskAlgoSdk.NskAlgoDataStream(NskAlgoDataType.NSK_ALGO_DATA_TYPE_ECG, buffer, 1);
                        Log.i(TAG, "onDataReceive NSK_ALGO_DATA_TYPE_ECG");
                    }
                    raw_index++;*//*

                }
            }*/

        }

        @Override
        public void onDataType(int type) {
            data_type = type;
//            heart = true;
//            ecg_xs_text.append(""+type);
            Log.i(TAG, "DataType"+type);
        }
    };






    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (cdTimer != null) {
//            cdTimer.cancel();
//            cdTimer = null;
//        }
//
//        if (countdowntimer!=null){
//            countdowntimer.cancel();
//            countdowntimer=null;
//        }
        ecgtext="";
        startButton.setText(getActivity().getText(R.string.str_measure));
        startButton.setEnabled(true);
        if (countdowntimer != null) {
            countdowntimer.cancel();
            countdowntimer = null;
        }

        if (countdowntimer1!=null){
            countdowntimer1.cancel();
            countdowntimer1=null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
//        if (bRunning) {
        nskAlgoSdk.NskAlgoStop();
        nskAlgoSdk.NskAlgoUninit();
        mSerial.setStop();
        //        }

        if (countdowntimer != null) {
                    countdowntimer.cancel();
                    countdowntimer = null;
        }

        if (countdowntimer1!=null){
            countdowntimer1.cancel();
            countdowntimer1=null;
        }
        ecgtext="";


//        if (cdTimer != null) {
//            cdTimer.cancel();
//            cdTimer = null;
//        }
//
//        if (ceshi!=null){
//            ceshi.cancel();
//            ceshi=null;
//        }

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.ecg_test);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("ecg",true);
    }
}
