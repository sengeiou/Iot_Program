package com.vtech.vhealth;

import android.app.Application;

import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import org.litepal.LitePal;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;


public class AppData extends Application {

    private static final long CONTIMEOUT = 10000L;
    private static final long READTIMEOUT = 30000L;
    private static final String TAG = "AppData";
    private static AppData mApp;

    @Override
    public void onCreate() {
        super.onCreate();
        mApp = this;
        initOkhttp();
        LitePal.initialize(this);
    }

    private void initOkhttp() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new LoggerInterceptor("TAG"))
                .connectTimeout(CONTIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(READTIMEOUT, TimeUnit.MILLISECONDS)
                //其他配置
                .build();
        OkHttpUtils.initClient(okHttpClient);
    }

    public static AppData get() {
        return mApp;
    }

}
