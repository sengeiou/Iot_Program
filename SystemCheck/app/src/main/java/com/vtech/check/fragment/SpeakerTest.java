package com.vtech.check.fragment;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;


import com.vtech.check.R;

import java.io.File;

public class SpeakerTest extends BaseTest implements OnPreparedListener {

    private static final String TAG = "SpeakerTest";
    private static final int MSG_DELAY_TIME = 5000;
    protected static final int MSG_START = 1;
    protected static final int MSG_END = 2;

    Ringtone r;

    private MediaPlayer mPlayer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.speaker, container, false);
        return v;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopMusic();
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mp.start();
    }

    @Override
    public void onHandleMessage(final int index) {
        switch (index) {
        case MSG_START:
            setTimerTask(MSG_END, MSG_DELAY_TIME);
            setButtonVisibility(false);
//            playMusic(R.raw.soundtest);
            playMusic();
            break;
        case MSG_END:
            setButtonVisibility(true);
            stopMusic();
            break;
        }
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        setTimerTask(MSG_START, 0);
        return true;
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.speaker_title);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("speaker", true);
    }

    protected void playMusic(int resId) {
        stopMusic();

        AssetFileDescriptor fd = getResources().openRawResourceFd(resId);
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource(fd.getFileDescriptor());
            /*, fd.getStartOffset(), fd.getLength()*/
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepare();
        } catch (Exception e) {
            Log.e(TAG, "E:" + e.getMessage());
        }
    }

    protected void playMusic() {//File file

        if (r!=null&&r.isPlaying()){
            return;
        }
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        r = RingtoneManager.getRingtone(getContext(),notification);
        r.play();

 /*       stopMusic();
        try {
            mPlayer = new MediaPlayer();
            mPlayer.setDataSource("/system/media/audio/ringtones/Honor.ogg");
//            mPlayer.setDataSource(file.getAbsolutePath());  	   SystemCheck \
            mPlayer.setOnPreparedListener(this);
            mPlayer.prepare();
        } catch (Exception e) {
            Log.e(TAG, "Music file not found");
        }*/
    }

    protected void stopMusic() {
        if (r != null) {
            r.stop();
            r = null;
        }
 /*       if (mPlayer != null) {
            mPlayer.stop();
            mPlayer.release();
            mPlayer = null;
        }*/

    }


}
