package com.vtech.check.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.view.View;
import android.widget.Button;

import com.vtech.check.MainActivity;
import com.vtech.check.R;

public class DisplayScreenActivity extends Activity {

    Button btn_auto;
    Button btn_single;
    Button btn_recover;
    Button btn_restore;
    private static final String PREFS = "test_prefs";
    private static final String KEY_SINGLE_MODE = "single_mode";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_screen);
        initView();
    }

    private void initView() {
        btn_auto=findViewById(R.id.btn_auto);
        btn_single=findViewById(R.id.btn_single);
        btn_recover=findViewById(R.id.btn_recover);
        btn_restore=findViewById(R.id.btn_restore);


        btn_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
                sp.edit().putBoolean(KEY_SINGLE_MODE, false).commit();
                Intent intent = new Intent(DisplayScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        btn_single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences(PREFS, MODE_PRIVATE);
                sp.edit().putBoolean(KEY_SINGLE_MODE, true).commit();
                Intent intent = new Intent(DisplayScreenActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btn_recover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
//                pm.reboot("reboot_recovery");
                PowerManager pManager = (PowerManager)getSystemService(Context.POWER_SERVICE);
                pManager.reboot("");

            }
        });
        btn_restore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    startFactoryDefault(getBaseContext());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void startFactoryDefault(Context context) throws Exception {
        if (Build.VERSION.SDK_INT < 26) {
            context.sendBroadcast(new Intent("android.intent.action.MASTER_CLEAR"));
        } else {
            Intent intent = new Intent("android.intent.action.FACTORY_RESET");
            intent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            intent.setPackage("android");
            context.sendBroadcast(intent);
        }
    }



}
