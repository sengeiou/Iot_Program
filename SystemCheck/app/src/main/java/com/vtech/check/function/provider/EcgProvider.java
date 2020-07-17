package com.vtech.check.function.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.vtech.check.constant.AnyKey;
import com.vtech.check.function.bean.EcgBean;

import org.litepal.LitePal;

/****
 * @author jason
 *  心电数据提供者
 * */
public class EcgProvider extends ContentProvider {
    private static final String TAG = "EcgProvider";
    public static final String AUTHORITY = "com.vtech.check.ecgprovider";

    @Override
    public boolean onCreate() {
        LitePal.initialize(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] strings, @Nullable String s, @Nullable String[] strings1, @Nullable String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }

    @Override
    public Bundle call(String method, String arg, Bundle extras) {

        Bundle bundle = new Bundle();
        EcgBean ecgBean = LitePal.findLast(EcgBean.class);
        if (ecgBean != null) {
            bundle.putString(AnyKey.KEY_HEALTH_ECG,ecgBean.getEcgData());
        }else {
            bundle.putString(AnyKey.KEY_HEALTH_ECG,"");
        }

        return bundle;
    }



}
