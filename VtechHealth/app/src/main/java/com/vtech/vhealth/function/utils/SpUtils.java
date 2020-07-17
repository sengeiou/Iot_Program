

package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.vtech.vhealth.AppData;

/**
 * 本地数据存储 sp
 *
 * @author jason
 */
public class SpUtils {

    private static volatile SharedPreferences sp;

    public static  SharedPreferences getSp() {
        if (sp == null) {
            synchronized (SpUtils.class){
                if (sp == null) {
                    Context context = AppData.get();
                    sp = context.getSharedPreferences(context.getPackageName(), Context.MODE_PRIVATE);
                }
            }
        }
        return sp;
    }

    /**
     * 清空所有数据
     */
    public static void clear() {
        getSp().edit().clear().apply();
    }

    /**
     * 写入.
     *
     * @param key   键.
     * @param value 值(boolean).
     */
    public static void set(String key, boolean value) {
        getSp().edit().putBoolean(key, value).apply();
    }

    /**
     * 写入.
     *
     * @param key   键.
     * @param value 值(float).
     */
    public static void set(String key, float value) {
        getSp().edit().putFloat(key, value).apply();
    }

    /**
     * 写入.
     *
     * @param key   键.
     * @param value 值(int).
     */
    public static void set(String key, int value) {
        getSp().edit().putInt(key, value).apply();
    }

    /**
     * 写入.
     *
     * @param key   键.
     * @param value 值(long).
     */
    public static void set(String key, long value) {
        getSp().edit().putLong(key, value).apply();
    }

    /**
     * 写入.
     *
     * @param key   键.
     * @param value 值(String).
     */
    public static void set(String key, String value) {
        getSp().edit().putString(key, value).apply();
    }

    /**
     * 移除指定键的值.
     *
     * @param key 键.
     */
    public static void remove(String key) {
        getSp().edit().remove(key).apply();
    }

    /**
     * 读取.
     *
     * @param key      键.
     * @param defValue 默认值(boolean).
     * @return 获取相应键的值, 若不存在此键则返回默认值.
     */
    public static boolean get(String key, boolean defValue) {
        return getSp().getBoolean(key, defValue);
    }

    /**
     * 读取.
     *
     * @param key      键.
     * @param defValue 默认值(float).
     * @return 获取相应键的值, 若不存在此键则返回默认值.
     */
    public static float get(String key, float defValue) {
        return getSp().getFloat(key, defValue);
    }

    /**
     * 读取.
     *
     * @param key      键.
     * @param defValue 默认值(int).
     * @return 获取相应键的值, 若不存在此键则返回默认值.
     */
    public static int get(String key, int defValue) {
        return getSp().getInt(key, defValue);
    }

    /**
     * 读取.
     *
     * @param key      键.
     * @param defValue 默认值(long).
     * @return 获取相应键的值, 若不存在此键则返回默认值.
     */
    public static long get(String key, long defValue) {
        return getSp().getLong(key, defValue);
    }

    /**
     * 读取.
     *
     * @param key      键.
     * @param defValue 默认值(String).
     * @return 获取相应键的值, 若不存在此键则返回默认值.
     */
    public static String get(String key, String defValue) {
        return getSp().getString(key, defValue);
    }

}
