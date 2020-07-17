package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.vtech.vhealth.function.main.newpressure.setting.SettingActivity;

import java.io.File;

public class UriUtils {

    public static  Uri getUri(Context context , File path){
        if (Build.VERSION.SDK_INT>=24){
            return  FileProvider.getUriForFile(context, SettingActivity.USER_AUTHORITY,path);
        }else {
            return Uri.fromFile(path);
        }
    }
}
