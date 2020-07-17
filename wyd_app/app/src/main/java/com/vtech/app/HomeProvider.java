package com.vtech.app;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.vtech.app.util.PreferenceConstants;
import com.vtech.app.util.PreferenceUtils;
import com.vtech.app.util.XmlUtils;


public class HomeProvider extends ContentProvider {
    public static final String TAG = "HomeProvider";
    public static final String AUTHORITY = "com.vtech.app.homeprovider";
    private Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String CMD_GET_TOKEN = "cmd_get_token";
    public static final String TOKEN = "token";

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
        Log.i(TAG, "call method : " + method);
        Bundle bundle = new Bundle();
        switch (method) {
            case CMD_GET_TOKEN:
                String p_token = PreferenceUtils.getPrefString(getContext(), PreferenceConstants.ACCESS_TOKEN, "");
                String a_token = App.getInstance().getToken();
                String token = TextUtils.isEmpty(a_token) ? (TextUtils.isEmpty(p_token) ? XmlUtils.readLocalXmlConfig() : p_token) : a_token;

                Log.i(TAG, "Preferences token == " + p_token + " App.getInstance().getToken() == " + App.getInstance().getToken() + " token : " + token);

                bundle.putString(TOKEN, token);
                return bundle;
        }
        return bundle;
    }
}