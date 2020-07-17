package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import java.util.Locale;

public class TokenUtils {
    public static final String TAG = "TokenUtils";
    public static final String AUTHORITY = "com.vtech.app.homeprovider";
    private static final Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String CMD_GET_TOKEN = "cmd_get_token";
    public static final String TOKEN = "token";

    public static String getToken(Context context) {
        String token = "";
        try {
            Bundle bundle = context.getContentResolver().call(URI, CMD_GET_TOKEN, "", null);
            if (bundle != null) {
                token = bundle.getString(TOKEN);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "getToken : " + token);
        return token;
    }

    /**
     * 获取系统语言
     *
     * @param context
     * @return
     */
    public static String getSystemLanguge(Context context) {
        Locale locale;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        String lang = locale.getLanguage() + "-" + locale.getCountry();

        return lang;
    }

    /**
     * 获取语言类别：  0简体 1翻译 2英文 必须填
     *
     * @param context
     * @return
     */
    public static int getLangugeType(Context context) {
        String lang = getSystemLanguge(context);
        if ("zh-CN".equals(lang)) {
            return 0;
        } else if ("zh-HK".equals(lang)) {
            return 1;
        } else if ("en".startsWith(lang)) {
            return 2;
        }
        return 0;
    }

}



