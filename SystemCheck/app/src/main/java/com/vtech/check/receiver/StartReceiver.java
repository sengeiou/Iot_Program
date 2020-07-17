package com.vtech.check.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.vtech.check.MainActivity;
import com.vtech.check.activity.DisplayScreenActivity;

import static android.provider.Telephony.Sms.Intents.SECRET_CODE_ACTION;

public class StartReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
/*        if (intent.getAction().equals(SECRET_CODE_ACTION)) {
            Intent i = new Intent(Intent.ACTION_MAIN);
            i.setClass(context, DisplayScreenActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }*/
    }
}
