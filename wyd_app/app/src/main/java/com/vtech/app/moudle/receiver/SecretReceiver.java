package com.vtech.app.moudle.receiver;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import com.vtech.app.util.Logger;

public class SecretReceiver extends BroadcastReceiver {
    public static final String TAG = "SecretReceiver";
    public static final String SECRET_CODE_ACTION = "android.provider.Telephony.SECRET_CODE";

    @Override
    public void onReceive(Context context, Intent intent) {
        Logger.i(TAG,"onReceive " + intent.getAction());
        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            Intent i = new Intent();
            ComponentName cn = new ComponentName("com.vtech.check", "com.vtech.check.activity.DisplayScreenActivity");
            try {
                i.setComponent(cn);
                context.startActivity(i);
            } catch (Exception e) {
                //TODO  可以在这里提示用户没有安装应用或找不到指定Activity，或者是做其他的操作
            }
        }
    }
}
