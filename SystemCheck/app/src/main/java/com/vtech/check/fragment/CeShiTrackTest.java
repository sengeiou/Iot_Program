package com.vtech.check.fragment;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.vtech.check.R;

public class CeShiTrackTest extends BaseTest{

    private static final int MSG_DELAY_TIME = 5000;
    protected static final int MSG_START = 1;
    protected static final int MSG_END = 2;

    View rootview;
    Button btn_cs;
    Button btn_tz;
    private int recBufSize = 0;

    private int playBufSize = 0;

    /**
     * 采样率（默认44100，每秒44100个点）
     */
    private int sampleRateInHz = 8000;

    /**
     * 声道（默认单声道）
     */
    private int channelConfig = AudioFormat.CHANNEL_CONFIGURATION_MONO;

    /**
     * 编码率（默认ENCODING_PCM_16BIT）
     */
    private int encodingBitrate = AudioFormat.ENCODING_PCM_16BIT;

    private AudioRecord audioRecord;

    private AudioTrack audioTrack;

    /**
     * 即时播放
     */
    private boolean blnInstantPlay = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.ceshitracktest, container, false);
        initView();
        return rootview;
    }

    private void initView() {
        btn_cs=rootview.findViewById(R.id.btn_cs);
        btn_cs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setTimerTask(MSG_START, 0);
            }
        });

        btn_tz=rootview.findViewById(R.id.btn_tz);
        btn_tz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blnInstantPlay = false;
            }
        });

    }


    @Override
    public void onHandleMessage(int index) {
        switch (index) {
            case MSG_START:
                setTimerTask(MSG_END, MSG_DELAY_TIME);
                setButtonVisibility(false);
                btn_cs.setText("回环测量中");
                btn_cs.setEnabled(false);
                initData();

                break;
            case MSG_END:
                setButtonVisibility(true);
                btn_cs.setText("点击回环测量");
                btn_cs.setEnabled(true);
                blnInstantPlay=false;
                break;
        }
    }

    private void initData() {
        recBufSize = AudioRecord.getMinBufferSize(sampleRateInHz,
                channelConfig, encodingBitrate);
        playBufSize = AudioTrack.getMinBufferSize(sampleRateInHz,
                channelConfig, encodingBitrate);

        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRateInHz, channelConfig, encodingBitrate, recBufSize);
        audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRateInHz,
                channelConfig, encodingBitrate, playBufSize, AudioTrack.MODE_STREAM);
        blnInstantPlay = true;
        new ThreadInstantPlay().start();
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        setTimerTask(MSG_START, 0);
        return true;
    }



    /**
     *
     * 即时播放线程
     *
     */
    class ThreadInstantPlay extends Thread
    {
        @Override
        public void run()
        {
            byte[] bsBuffer = new byte[recBufSize];
            audioRecord.startRecording();
            audioTrack.play();
            while(blnInstantPlay)
            {

                int line = audioRecord.read(bsBuffer, 0, recBufSize);

                byte[] tmpBuf = new byte[line];

                System.arraycopy(bsBuffer, 0, tmpBuf, 0, line);

                audioTrack.write(tmpBuf, 0, tmpBuf.length);
            }
            audioTrack.stop();
            audioRecord.stop();
        }
    }



    @Override
    public String getTestName() {
        return  getContext().getString(R.string.loopback_title);
    }


    @Override
    public boolean isNeedTest() {
        return getSystemProperties("loopback", true);
    }

}
