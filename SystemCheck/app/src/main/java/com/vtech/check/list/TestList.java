package com.vtech.check.list;

import android.content.Context;

import com.vtech.check.fragment.BacklightTest;
import com.vtech.check.fragment.BaseTest;
import com.vtech.check.fragment.BatteryTest;
import com.vtech.check.fragment.BloodTest;
import com.vtech.check.fragment.BlutoothTest;
import com.vtech.check.fragment.CESHITest;
import com.vtech.check.fragment.CameraTest;
import com.vtech.check.fragment.CeShiTrackTest;
import com.vtech.check.fragment.ClockTest;
import com.vtech.check.fragment.EthernetTest;
import com.vtech.check.fragment.KeyTest;
import com.vtech.check.fragment.LCDTest;
import com.vtech.check.fragment.SIMTest;
import com.vtech.check.fragment.SpeakerTest;
import com.vtech.check.fragment.TelephoneTest;
import com.vtech.check.fragment.TouchTest;
import com.vtech.check.fragment.UnknownTest;
import com.vtech.check.fragment.VersionTest;
import com.vtech.check.fragment.WlanTest;


public class TestList {
    //要测试的项目
    private static final BaseTest[] ALL_ITEMS = {
            new VersionTest(),
            new BlutoothTest(),
            new LCDTest(),
            new KeyTest(),
            new CameraTest(),
            new TouchTest(),
            new WlanTest(),
            new ClockTest(),
            new BacklightTest(),
//            new ReceiverTest(),
            new SpeakerTest(),
            new CeShiTrackTest(),
//            new LoopbackTest(),     //音频回环测试
            new SIMTest(),
            new BatteryTest(),
            /*new IndicatorLightTest(),*/  //指示灯
            new TelephoneTest(),
            new EthernetTest(),
            new BloodTest(),
//            new ECGTest(),
            new CESHITest(),



    };

    //需要测试的item
    private static BaseTest[] sItems;

    //不知道测试的item
    private static BaseTest sUnknownTest = new UnknownTest();

    public static void updateItems(Context context) {
        sUnknownTest.setContext(context);
        int size = 0;
        for (BaseTest t : ALL_ITEMS) {
            t.setContext(context);
            if (t.isNeedTest()) {
                size++;
            }
        }
        sItems = new BaseTest[size];

        int i = 0;
        for (BaseTest t : ALL_ITEMS) {
            if (t.isNeedTest()) {
                sItems[i] = t;
                i++;
            }
        }
    }

    public static int getCount() {
        return sItems.length;
    }

    public static BaseTest get(int position) {
        if (position >= 0 && position < getCount()) {
            return sItems[position];
        } else {
           return  sUnknownTest;
        }
    }

}
