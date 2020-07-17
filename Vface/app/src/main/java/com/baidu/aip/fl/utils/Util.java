/*
 * Copyright (C) 2017 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.fl.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.UUID;

import okio.Buffer;

// copied from OKHttp
public class Util {
    private static final String TAG = "Util";

    private static final char[] HEX_DIGITS = {'0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    private static boolean percentEncoded(String encoded, int pos, int limit) {
        return pos + 2 < limit
                && encoded.charAt(pos) == '%'
                && decodeHexDigit(encoded.charAt(pos + 1)) != -1
                && decodeHexDigit(encoded.charAt(pos + 2)) != -1;
    }

    private static int decodeHexDigit(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        return -1;
    }

    /**
     * Returns a substring of {@code input} on the range {@code [pos..limit)} with the following
     * transformations:
     * <ul>
     * <li>Tabs, newlines, form feeds and carriage returns are skipped.
     * <li>In queries, ' ' is encoded to '+' and '+' is encoded to "%2B".
     * <li>Characters in {@code encodeSet} are percent-encoded.
     * <li>Control characters and non-ASCII characters are percent-encoded.
     * <li>All other characters are copied without transformation.
     * </ul>
     *
     * @param alreadyEncoded true to leave '%' as-is; false to convert it to '%25'.
     * @param strict         true to encode '%' if it is not the prefix of a valid percent encoding.
     * @param plusIsSpace    true to encode '+' as "%2B" if it is not already encoded.
     * @param asciiOnly      true to encode all non-ASCII codepoints.
     */
    private static String canonicalize(String input, int pos, int limit, String encodeSet,
                                       boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly) {
        int codePoint;
        for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
            codePoint = input.codePointAt(i);
            if (codePoint < 0x20
                    || codePoint == 0x7f
                    || codePoint >= 0x80 && asciiOnly
                    || encodeSet.indexOf(codePoint) != -1
                    || codePoint == '%' && (!alreadyEncoded || strict && !percentEncoded(input, i, limit))
                    || codePoint == '+' && plusIsSpace) {
                // Slow path: the character at i requires encoding!
                Buffer out = new Buffer();
                out.writeUtf8(input, pos, i);
                canonicalize(out, input, i, limit, encodeSet, alreadyEncoded, strict, plusIsSpace,
                        asciiOnly);
                return out.readUtf8();
            }
        }

        // Fast path: no characters in [pos..limit) required encoding.
        return input.substring(pos, limit);
    }

    private static void canonicalize(Buffer out, String input, int pos, int limit, String encodeSet,
                                     boolean alreadyEncoded, boolean strict, boolean plusIsSpace, boolean asciiOnly) {
        Buffer utf8Buffer = null; // Lazily allocated.
        int codePoint;
        for (int i = pos; i < limit; i += Character.charCount(codePoint)) {
            codePoint = input.codePointAt(i);
            if (alreadyEncoded
                    && (codePoint == '\t' || codePoint == '\n' || codePoint == '\f' || codePoint == '\r')) {
                // TODO Skip this character.
                LogUtil.d(Util.class.getName(), "codePoint:" + codePoint);
                // TODO delete this
            } else if (codePoint == '+' && plusIsSpace) {
                // Encode '+' as '%2B' since we permit ' ' to be encoded as either '+' or '%20'.
                out.writeUtf8(alreadyEncoded ? "+" : "%2B");
            } else if (codePoint < 0x20
                    || codePoint == 0x7f
                    || codePoint >= 0x80 && asciiOnly
                    || encodeSet.indexOf(codePoint) != -1
                    || codePoint == '%' && (!alreadyEncoded || strict && !percentEncoded(input, i, limit))) {
                // Percent encode this character.
                if (utf8Buffer == null) {
                    utf8Buffer = new Buffer();
                }
                utf8Buffer.writeUtf8CodePoint(codePoint);
                while (!utf8Buffer.exhausted()) {
                    int b = utf8Buffer.readByte() & 0xff;
                    out.writeByte('%');
                    out.writeByte(HEX_DIGITS[(b >> 4) & 0xf]);
                    out.writeByte(HEX_DIGITS[b & 0xf]);
                }
            } else {
                // This character doesn't need encoding. Just copy it over.
                out.writeUtf8CodePoint(codePoint);
            }
        }
    }

    static String canonicalize(String input, String encodeSet, boolean alreadyEncoded, boolean strict) {
        return canonicalize(
                input, 0, input.length(), encodeSet, alreadyEncoded, strict, true, true);
    }

    /**
     * 对字符串md5加密
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        try {
            // 生成一个MD5加密计算摘要
            MessageDigest md = MessageDigest.getInstance("MD5");
            // 计算md5函数
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 判断设备是否是手机
     */
    public static boolean isPhone(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getPhoneType() != TelephonyManager.PHONE_TYPE_NONE;
    }

    /**
     * 获取当前设备的IMIE，需与上面的isPhone一起使用
     * 需添加权限<uses-permission android:name="android.permission.READ_PHONE_STATE"/>
     */
    public static String getDeviceIMEI(Context context) {
        String deviceId;
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (isPhone(context)) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.e("getDeviceIMEI", "没有相应权限");
                return "";
            }
            deviceId = tm.getDeviceId();
        } else {
            deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = tm.getSimSerialNumber();
        }

        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getUUID(context);
        }

        Log.e("getDeviceIMEI", "deviceId : " + deviceId);
        return deviceId;
    }


    /**
     * 得到全局唯一UUID
     *
     * @return
     */
    public static String getUUID(Context context) {
        String uuid = "";
        SharedPreferences mShare = context.getSharedPreferences("uuid", context.MODE_PRIVATE);
        if (mShare != null) {
            uuid = mShare.getString("uuid", "");
        }
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            mShare.edit().putString("uuid", uuid).commit();
        }
        return uuid;
    }

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
