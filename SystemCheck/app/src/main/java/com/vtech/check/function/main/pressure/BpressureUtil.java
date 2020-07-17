package com.vtech.check.function.main.pressure;

import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;


import com.vtech.check.function.provider.DbOpenHelper;
import com.vtech.check.function.provider.SerialProvider;
import com.vtech.check.function.pserial.SerialPortUtil;
import com.vtech.check.function.utils.JsonUtil;
import com.vtech.check.function.bean.HealthDTO;

import java.util.LinkedList;
import java.util.Queue;

public class BpressureUtil {

    private static final int SON_MSG_DATA_UPDATE = 3030;
    private static final int SON_MSG_PLUS_UPDATE = 3031;
    private static final int SON_MSG_ECG_UPDATE = 3032;
    private static final int SON_MSG_PPG_UPDATE = 3034;
    private static final int SON_MSG_STATE_UPDATE = 3036;
    private static final int SON_MSG_CALIBRATION = 3038;
    private static final int SON_MSG_CLEAR_CALIBRATION = 3040;
    //数据采集循环周期 1S
    private static final int SON_DATA_CYCLE_TIME = 1000;
    private static final int SON_SIGN_CYCLE_TIME = 200;


    private static final byte[] SON_CMD_UPDATE_DATA = {(byte) (0xfd & 0xff), 0x00, 0x00, 0x00};
    private static final byte[] SON_CMD_UPDATE_PULSE = {(byte) (0xfc & 0xff), 0x00, 0x00, 0x00};
    private static final byte[] SON_CMD_UPDATE_ECG = {(byte) (0xf9 & 0xff), 0x00, 0x00, 0x00};
    private static final byte[] SON_CMD_UPDATE_PPG = {(byte) (0xf5 & 0xff), 0x00, 0x00, 0x00};
    private static final byte[] SON_CMD_GET_STATE = {(byte) (0xf8 & 0xff), 0x00, 0x00, 0x00};
    private static final byte[] SON_CMD_CLEAR_CALIBRATION = {(byte) (0xfa & 0xff), 0x00, 0x00, 0x00};
    private static byte[] SON_CMD_UPDATE_CALIBRATION;


    private static final String TAG = "BpressureUtil";

    private static volatile BpressureUtil instance;
    private static Context context;

    private static int HU_STATUS_START = 0x1000;
    private static int HU_STATUS_STOP = 0x1001;

    private int iLSBP, iSBP = 0;  //收缩压 高压
    private int iLDBP, iDBP = 0;  //舒张压 低压
    private int iLHrate, iHRate = 0; //心率
    private int iBpressure = 0;
    private int iPluse = 0;
    private int iEcg = 0;
    private int status = 0;
    //0 成功 1 校准中，2 失败 -1 默认状态 -1
    private int calibrationStatus = -1;
    //清除校准数据状态 0 清除成功，1 清除失败 默认状态 -1
    private int clearCalibration = -1;
    //脉搏 队列
    private Queue<Integer> mPluse = new LinkedList<>();
    //心电
    private Queue<Integer> mEcg = new LinkedList<>();
    //数据对象传输用
    private HealthDTO mHealthDTOBean = new HealthDTO();
    private static final int MAXCOUNT = 10;


    private SerialPortUtil mSerial;

    private int iStatus = HU_STATUS_STOP;
    private boolean bWorking = false;
    private int mOfflineCount = 0;
    private int mInlineCount = 0;
    private String userId;
    private String deviceId;
    //是否处于校准状态
    private boolean calibration;


    /**
     * 获取实例，线程安全
     *
     * @return
     */
    public static BpressureUtil getInstance(Context context) {
        if (instance == null || BpressureUtil.context != context) {
            if (instance == null) {
                synchronized (BpressureUtil.class) {
                    instance = new BpressureUtil(context);
                }
            }

        }
        return instance;
    }

    public BpressureUtil(Context context) {
        this.context = context;
        initHealth();
    }


    public String getHealthData() {
        Integer[] arrPluse = mPluse.toArray(new Integer[mPluse.size()]);
        Integer[] arrEcg = mEcg.toArray(new Integer[mEcg.size()]);
        mHealthDTOBean.setmEcg(arrEcg);
        mHealthDTOBean.setmPluse(arrPluse);
        mHealthDTOBean.setiDBP(iDBP);
        mHealthDTOBean.setiSBP(iSBP);
        mHealthDTOBean.setiHRate(iHRate);
        mHealthDTOBean.setUserId(userId);
        mHealthDTOBean.setDeviceId(deviceId);
        mHealthDTOBean.setCalibrationStatus(calibrationStatus);
        mHealthDTOBean.setClearCalibration(clearCalibration);
        String string = JsonUtil.beanToJson(mHealthDTOBean);
        Log.w(TAG, "getHealthData " + string);
        return string;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    //开始监测
    public void start() {
        if (iStatus != HU_STATUS_START) {
            calibration = false;
            clearCalibration = -1;
            iStatus = HU_STATUS_START;
            dataReset();
            mSerial.setStart();
            mSerial.setOnDataReceiveListener(serialReceiveListener);
            healthHandler.sendEmptyMessage(SON_MSG_STATE_UPDATE);
            bWorking = false;
            mOfflineCount = 0;
            mInlineCount = 0;

        } else {
            Log.w(TAG, "Health util already started");
        }
    }
    //设置校准数据
    public void setCalionration(byte[] Calibration) {
        SON_CMD_UPDATE_CALIBRATION = Calibration;
        healthHandler.sendEmptyMessage(SON_MSG_CALIBRATION);
    }
        //清除校准信息
    public void sendClearCalion(){
        healthHandler.sendEmptyMessage(SON_MSG_CLEAR_CALIBRATION);
    }

    //停止监测
    public void stop() {
        if (iStatus != HU_STATUS_STOP) {
            iStatus = HU_STATUS_STOP;
            mSerial.setStop();
            bWorking = false;
            calibration = false;
            mOfflineCount = 0;
            mInlineCount = 0;
            calibrationStatus=-1;
            clearCalibration=-1;
            context = null;
            instance = null;
            healthHandler.removeCallbacksAndMessages(null);
            dataReset();
        } else {
            Log.w(TAG, "Health util already stoped");
        }
    }

    private void dataReset() {
        iSBP = 0;  //收缩压 高压
        iDBP = 0;  //舒张压 低压
        iHRate = 0; //心率
        iBpressure = 0;
        iPluse = 0;
        iEcg = 0;
        mPluse.clear();
        mEcg.clear();
    }

    private void initHealth() {
        Log.w(TAG, "on initHealth");
        if (null == mSerial) {
            mSerial = new SerialPortUtil(SerialPortUtil.RESPIRE_DEV_PATH);
            mSerial.onCreate();

        }
    }

    private void parseSonData(byte[] buffer) {

        iSBP = buffer[1] & 0xff;
        if (iSBP == 0xff)
            iSBP = 0;

        iDBP = buffer[2] & 0xff;
        if (iDBP == 0xff)
            iDBP = 0;

        iHRate = buffer[3] & 0xff;
        if (iHRate == 0xff)
            iHRate = 0;

        if (iSBP != 0)
            iLSBP = iSBP;

        if (iDBP != 0)
            iLDBP = iDBP;

        if (iHRate != 0)
            iLHrate = iHRate;
//        Log.w(TAG, " onDataReceive 时间：" + BpressureTimeUtil.convertTimeToStr(System.currentTimeMillis()) + " 收缩压 SBP:" + iSBP + " 舒张压 DBP: " + iDBP + " 心率 HRate：" + iHRate);
        if (iHRate > 0 && iSBP > 0 && iDBP > 0) {
            if (context != null) {
                ContentValues mContentValues = new ContentValues();
                mContentValues.put(DbOpenHelper.HEALTH_IDBP, iDBP);
                mContentValues.put(DbOpenHelper.HEALTH_ISBP, iSBP);
                mContentValues.put(DbOpenHelper.HEALTH_IHRATE, iHRate);
                mContentValues.put(DbOpenHelper.HEALTH_STATUS, 0);
                mContentValues.put(DbOpenHelper.HEALTH_CREATETIME, BpressureTimeUtil.getCurTime());
                mContentValues.put(DbOpenHelper.HEALTH_DEVICESID, deviceId);
                mContentValues.put(DbOpenHelper.HEALTH_USERID, userId);
                //插入数据到ContentProviderServer的数据库
                context.getContentResolver().insert(SerialProvider.uri, mContentValues);
                Log.w(TAG, " 有效数据 插入成功：" + BpressureTimeUtil.getCurTime() + " 收缩压 SBP:" + iSBP + " 舒张压 DBP: " + iDBP + " 心率 HRate：" + iHRate);
            }
        }
    }


    SerialPortUtil.OnDataReceiveListener serialReceiveListener = new SerialPortUtil.OnDataReceiveListener() {
        @Override
        public void onDataReceive(byte[] buffer, int size) {

            if (!((size == 6) || (size == 12))) {
                Log.w(TAG, "mSerialPU2 buffer start with " + buffer[0] + "buf size is " + size + " 6 statet is " + buffer[6]);
                return;
            }
            switch (buffer[0] & 0xff) {
                //状态返回至
                case 0xF8:
                    ///校准中不检测手指状态
                    if (calibration) {
                        return;
                    }
                    //04 脱手状态 07|39|05  测试中
                    Log.w(TAG, "status current status is " + buffer[3]);
                    status = buffer[3];
                    if (status == 4) {
                        mOfflineCount++;
                    } else if (status == 5) {
                        //调光
                    } else {
                        mInlineCount++;
                        mOfflineCount = 0;
                    }

                    if (mInlineCount >= 6) {
                        if (!bWorking) {
                            healthHandler.sendEmptyMessage(SON_MSG_DATA_UPDATE);
                            healthHandler.sendEmptyMessage(SON_MSG_PLUS_UPDATE);
                            //  healthHandler.sendEmptyMessage(SON_MSG_ECG_UPDATE);
                            bWorking = true;
                            mOfflineCount = 0;
                        }
                    }

                    if (mOfflineCount >= 20) {
                        mOfflineCount = 0;
                        mInlineCount = 0;
                        bWorking = false;
//                        SpeechUtil.getInstance(context).speak("请放好手指");
//                        PlayVoiceUtil.play(context, "请放好手指");

                    }
                    break;
                //
                case 0xFD:
                    parseSonData(buffer);
                    break;
                //脉搏
                case 0xFC:
                    parsePluse(buffer);
//                    Log.w(TAG, "onDataReceive plus value is " + iPluse);
                    break;
                //ECG
                case 0xF9:
                    parseECG(buffer);
//                    Log.w(TAG, "onDataReceive ecg value is " + (int)(buffer[2]<<8|buffer[3]));
                    break;
                //PPG
                case 0xF5:
                    parsePPG(buffer);
                    //Log.w(TAG, "onDataReceive plus ppg is " + iPluse);
                    break;
                case 0xFE:
                    Log.w(TAG, "onDataReceive parseCalibration FE ");
                    parseCalibration(buffer);
                    break;
                case 0xFA:
                    clearCalibration =0;
                    Log.w(TAG, "onDataReceive clear caliration  清除校准成功  "+clearCalibration);
                    break;
                case 0xFB:
                    Log.w(TAG, "onDataReceive parseCalibration FB ");
                    parseCalibration(buffer);
                    break;
                default:
                    Log.w(TAG, " default  onDataReceive undow data " + buffer[0] + " " + buffer[1] + " " + buffer[2] + " " + buffer[3]);
                    break;
            }

            return;

        }
    };
    //解析校准数据
    private void parseCalibration(byte[] buffer) {
        int calibration = buffer[3] & 0xff;
        calibrationStatus=calibration;
        Log.w(TAG, "onDataReceive parseCalibration  "+calibration);

    }

    private void parsePluse(byte[] buffer) {
        int high = buffer[2] & 0xff;
        int low = buffer[3] & 0xff;

        if (status == 4) {//离手状态
            iPluse = 0;
        } else {
            iPluse = high * 255 + low;
        }

//        Log.w(TAG, " pluse   zjs   脉搏=" + iPluse);
        if (mPluse.size() >= MAXCOUNT) {//最多保持 60个数据 20ms
            mPluse.poll();//最早的一个数据丢弃
        }
        mPluse.add(iPluse);
    }

    private void parseECG(byte[] buffer) {
//        Log.w(TAG, " zhang  parseEcg  " + BpressureTimeUtil.convertTimeToStr(System.currentTimeMillis()));
        int high = buffer[2] & 0xff;
        int low = buffer[3] & 0xff;
        if (high == 0xff) {
            high = 0;
        }
        if (low == 0xff) {
            low = 0;
        }

        if (status == 4) {//离手状态
            iEcg = 0;
        } else {
            iEcg = high * 255 + low;
        }

        if (mEcg.size() >= MAXCOUNT) {//最多保持 60个数据 100ms
            mEcg.poll();//最早的一个数据丢弃
        }
        mEcg.add(iEcg);

    }

    private void parsePPG(byte[] buffer) {
//        Log.w(TAG, " zhang  parseEcg  " + BpressureTimeUtil.convertTimeToStr(System.currentTimeMillis()));
        int high = buffer[1] & 0xff;
        int low = buffer[2] & 0xff;
        int rate = buffer[3] & 0xff;
        // 4 5 保留

        if (high == 0xff) {
            high = 0;
        }
        if (low == 0xff) {
            low = 0;
        }
        if (rate == 0xff) {
            rate = 0;
        }

        int sing1 = buffer[6] & 0xff;
        int sing2 = buffer[7] & 0xff;
        int sing3 = buffer[8] & 0xff;

        if (sing1 == 0xff) {
            sing1 = 0;
        }
        if (sing2 == 0xff) {
            sing2 = 0;
        }
        if (sing3 == 0xff) {
            sing3 = 0;
        }

    }


    //健康handler  用于定时循环
    private Handler healthHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            Log.d(TAG, "healthRunnable： handleMessage==== " + msg.what);
            switch (msg.what) {
                //清除校准数据
                case  SON_MSG_CLEAR_CALIBRATION:
                    mSerial.sendBuffer(SON_CMD_CLEAR_CALIBRATION);
                    break;
                //发送校准数据给芯片
                case SON_MSG_CALIBRATION:
                    if (SON_CMD_UPDATE_CALIBRATION == null || SON_CMD_UPDATE_CALIBRATION.length <= 0) {
                        return;
                    }
                    mSerial.sendBuffer(SON_CMD_UPDATE_CALIBRATION);
                    break;
                case SON_MSG_DATA_UPDATE:
//                    Log.d(TAG, "healthRunnable： SON_MSG_DATA_UPDATE。 " + msg.what);
                    mSerial.sendBuffer(SON_CMD_UPDATE_DATA);
                    sendEmptyMessageDelayed(SON_MSG_DATA_UPDATE, SON_DATA_CYCLE_TIME);
                    break;
                case SON_MSG_ECG_UPDATE:
//                    Log.d(TAG, "healthRunnable： SON_MSG_ECG_UPDATE。 " + msg.what);
                    mSerial.sendBuffer(SON_CMD_UPDATE_ECG);
                    sendEmptyMessageDelayed(SON_MSG_ECG_UPDATE, SON_SIGN_CYCLE_TIME);
                    break;
                case SON_MSG_STATE_UPDATE:
//                    Log.d(TAG, "healthRunnable： SON_MSG_STATE_UPDATE。 " + msg.what);
                    mSerial.sendBuffer(SON_CMD_GET_STATE);
                    sendEmptyMessageDelayed(SON_MSG_STATE_UPDATE, SON_SIGN_CYCLE_TIME);
                    break;
                case SON_MSG_PLUS_UPDATE:
//                    Log.d(TAG, "healthRunnable： SON_MSG_PLUS_UPDATE。 " + msg.what);
                    mSerial.sendBuffer(SON_CMD_UPDATE_PULSE);
                    sendEmptyMessageDelayed(SON_MSG_PLUS_UPDATE, SON_SIGN_CYCLE_TIME);
                    break;
                case SON_MSG_PPG_UPDATE:
//                    Log.d(TAG, "healthRunnable： SON_MSG_PPG_UPDATE。 " + msg.what);
                    mSerial.sendBuffer(SON_CMD_UPDATE_PPG);
                    sendEmptyMessageDelayed(SON_MSG_PPG_UPDATE, SON_SIGN_CYCLE_TIME);
                    break;
                default:
                    break;


            }
        }
    };


    public static void main(String[] args) {
        int valueTen = 255;
        byte[] bytes;
//将其转换为十六进制并输出
        String strHex = Integer.toHexString(valueTen);
        System.out.println("==" + strHex);

        bytes = strHex.getBytes();
        System.out.println("=bytes.length=" + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            System.out.println("=bytes[" + i + "]" + bytes[i]);
            System.out.println("=char[" + i + "]" + (char)(bytes[i]));
        }

        valueTen = 160;
        strHex = Integer.toHexString(valueTen);
        System.out.println("==" + strHex);

        bytes = strHex.getBytes();
        System.out.println("=bytes.length=" + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            System.out.println("=bytes[" + i + "]" + bytes[i]);
            System.out.println("=char[" + i + "]" + (char)(bytes[i]));
        }


        valueTen = 100;
        strHex = Integer.toHexString(valueTen);
        System.out.println("==" + strHex);

        bytes = strHex.getBytes();
        System.out.println("=bytes.length=" + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            System.out.println("=bytes[" + i + "]" + bytes[i]);
            System.out.println("=char[" + i + "]" + (char)(bytes[i]));
        }


        valueTen = 80;
        strHex = Integer.toHexString(valueTen);
        System.out.println("==" + strHex);


        bytes = strHex.getBytes();
        System.out.println("=bytes.length=" + bytes.length);
        for (int i = 0; i < bytes.length; i++) {
            System.out.println("=bytes[" + i + "]" + bytes[i]);
            System.out.println("=char[" + i + "]" + (char)(bytes[i]));
        }


    }

}
