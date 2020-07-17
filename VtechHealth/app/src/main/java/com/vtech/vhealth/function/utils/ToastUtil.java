

package com.vtech.vhealth.function.utils;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.vtech.vhealth.AppData;


/**
 * 公共的Toast
 */
public class ToastUtil {

    private static Toast sToast = null;

    public static Toast show(Context context, String msg){
        return show(context, msg, Toast.LENGTH_SHORT);
    }

    public static Toast show(Context context, String msg, int duration) {

        if(TextUtils.isEmpty(msg) || context == null){
            return null;
        }

        if(duration < Toast.LENGTH_SHORT){
            duration = Toast.LENGTH_SHORT;
        }

        if (sToast == null) {
            sToast = Toast.makeText(context, msg, duration);
        } else {
            sToast.setDuration(duration);
            sToast.setText(msg);
        }
        sToast.show();
        LogUtil.show("ToastUtil", msg);
        return sToast;
    }

    public static Toast show(String msg, int duration) {
        if(AppData.get() == null){
            return null;
        }
        return show(AppData.get(), msg, duration);
    }

    /**
     * show long Toast
     * @param msg
     * @return
     */
    public static Toast showLong(String msg) {
        return show(msg, Toast.LENGTH_LONG);
    }

    public static Toast showLong(int msg) {
        if(AppData.get() == null){
            return null;
        }
        return show(AppData.get().getString(msg), Toast.LENGTH_LONG);
    }

    /**
     * short show Toast
     * @param msg
     * @return
     */
    public static Toast showShort(String msg) {

        return show(msg, Toast.LENGTH_SHORT);
    }

    public static Toast showShort(int msg) {
        if(AppData.get() == null){
            return null;
        }
        return show(AppData.get().getResources().getString(msg), Toast.LENGTH_SHORT);
    }


}