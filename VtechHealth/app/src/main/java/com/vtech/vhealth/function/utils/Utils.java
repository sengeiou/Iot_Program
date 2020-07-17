package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
    public static final String TAG = "Util";
    private static final String AUTHORITY = "com.vtech.voiceassistant.comprovider";
    private static Uri URI = Uri.parse("content://" + AUTHORITY);
    public static final String CASE_GET_COM = "getComdata";
    public static final String KEY_COM_PROVIDER = "comprovider";

    public static String getDeviceId(Context context) {
        Bundle bundle = context.getContentResolver().call(URI, CASE_GET_COM, "", null);
        if (bundle != null) {
            String comDataStr = bundle.getString(KEY_COM_PROVIDER);
            Log.i(TAG, "getDeviceId data : " + comDataStr);
            if (!TextUtils.isEmpty(comDataStr)) {
//                mComData = JsonUtil.jsonToBean(comDataStr, ComData.class);
                try {
                    JSONObject jsonObject = new JSONObject(comDataStr);
                    String deviceId = jsonObject.optString("deviceId");
                    Log.i(TAG, "getDeviceId deviceId : " + deviceId);

                    return deviceId;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

}
