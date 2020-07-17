package com.vtech.check;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import androidx.multidex.MultiDex;

import com.vtech.check.list.TestList;

import org.litepal.LitePal;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        TestList.updateItems(getBaseContext());
        LitePal.initialize(this);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        if(newConfig.fontScale != 1){//非默认值
            getResources();
        }
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public Resources getResources() {

        Resources res = super.getResources();
        if(res.getConfiguration().fontScale != 1){
            Configuration newConfig = new Configuration();
            newConfig.setToDefaults();//设置为默认值
            res.updateConfiguration(newConfig,res.getDisplayMetrics());
        }

        return res;
    }

}
