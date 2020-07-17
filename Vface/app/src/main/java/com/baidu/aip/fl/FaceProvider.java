package com.baidu.aip.fl;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.baidu.aip.fl.utils.PreferenceUtils;

public class FaceProvider extends ContentProvider {
    public static final String AUTHORITY = "com.vtech.face.faceprovider";
    private Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String CMD_IS_REGIST = "cmd_is_regist";
    public static final String IS_REGIST = "is_regist";

    @Override
    public boolean onCreate() {
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

    @Nullable
    @Override
    public Bundle call(@NonNull String method, @Nullable String arg, @Nullable Bundle extras) {
        Bundle bundle = new Bundle();
        try {
            switch (method) {
                case CMD_IS_REGIST:
                    boolean isRegist = PreferenceUtils.getPrefBoolean(getContext(), IS_REGIST, false);
                    bundle.putBoolean(IS_REGIST, isRegist);
                    return bundle;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bundle;
    }
}
