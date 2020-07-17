package com.vtech.check.fragment;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.vtech.check.R;

import static android.content.Context.NOTIFICATION_SERVICE;


public class IndicatorLightTest extends BaseTest{

    View rootview;
    Button btn_open;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootview==null){
            rootview=inflater.inflate(R.layout.indicator,container,false);
        }
        initView();
        return rootview;
    }

    private void initView() {
        btn_open=rootview.findViewById(R.id.btn_open);
        btn_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //华为呼吸灯有bug
                NotificationManager nm=(NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
                Notification notification = new Notification();
                notification.ledARGB = Color.GREEN; //这里是颜色，我们可以尝试改变，理论上0xFFff0000是红色
                notification.ledOnMS = 1000;
                notification.ledOffMS = 1000;
                notification.flags = Notification.FLAG_SHOW_LIGHTS;
                nm.notify(1, notification);

            }
        });
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.indicator_test);
    }


    @Override
    public boolean isNeedTest() {
        return getSystemProperties("indicator",true);
    }
}
