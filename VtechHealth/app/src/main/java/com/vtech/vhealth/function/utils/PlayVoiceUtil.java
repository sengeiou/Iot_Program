package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

//发送广播给语音助手播放指定字符串
public class PlayVoiceUtil {


    private final static String PLAYVOICEACTION = "com.vtech.voiceassistant.TTS_PLAY";
    private final static String TTS_KEY = "tts_str";

    public static final void play(Context context, String voice) {
        if (!TextUtils.isEmpty(voice)) {
            Intent playIntent = new Intent(PLAYVOICEACTION);
            playIntent.putExtra(TTS_KEY, voice);
            //FLAG_RECEIVER_INCLUDE_BACKGROUND = 0x01000000;
            playIntent.setFlags(0x01000000);
            context.sendBroadcast(playIntent);

        }
    }
}
