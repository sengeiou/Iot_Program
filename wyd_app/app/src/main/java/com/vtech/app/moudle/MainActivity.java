package com.vtech.app.moudle;

import android.content.*;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;

import com.vtech.app.App;
import com.vtech.app.BuildConfig;
import com.vtech.app.R;
import com.vtech.app.data.APIService;
import com.vtech.app.largeuidb.Constants;
import com.vtech.app.largeuidb.dbutils.ItemUtil;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;
import com.vtech.app.moudle.adapter.MainPagerAdapter;
import com.vtech.app.moudle.adapter.MainPagerAdapterLargeUI;
import com.vtech.app.moudle.largeui.LargeUISecondFragment;
import com.vtech.app.moudle.main.MainFragment;
import com.vtech.app.moudle.largeui.TimeWeatherFragment;
import com.vtech.app.moudle.receiver.BootReceiver;
import com.vtech.app.moudle.receiver.LocaleChangeReceiver;
import com.vtech.app.util.Logger;
import com.vtech.app.util.Utils;
import com.vtech.app.view.BaseViewPager;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    RadioGroup radioGroup;
    BaseViewPager viewPager;
    MainPagerAdapter mMainPagerAdapter;

    private Fragment[] mFragments = new Fragment[3];

    private int mCurrAppCnt = 0;

    private List<Fragment> fragments_LargeUI = new ArrayList<>();

    private MainPagerAdapterLargeUI adapterLargeUI;

    @Override
    protected int getLayoutID() {
        if (BuildConfig.LARGE_UI_SWITCH) {
            return R.layout.activity_main_largeui;
        } else {
            return R.layout.activity_main;
        }
    }

    @Override
    protected void initView() {
        if (!BuildConfig.LARGE_UI_SWITCH) {
            radioGroup = (RadioGroup) findViewById(R.id.radio_group);
            viewPager = findViewById(R.id.vp_horizontal_ntb);
            mMainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), mFragments);
            viewPager.setAdapter(mMainPagerAdapter);
            viewPager.setOffscreenPageLimit(1);
            viewPager.setCurrentItem(0);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int i) {
                    switch (i) {
                        case 0:
                            radioGroup.check(R.id.report);
                            break;
                        case 1:
                            radioGroup.check(R.id.main);
                            break;
                        case 2:
                            radioGroup.check(R.id.home);
                            break;
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                    switch (checkedId) {
                        case R.id.report:
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.main:
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.home:
                            viewPager.setCurrentItem(2);
                            break;
                    }
                }
            });
        } else {
            initView_LargeUI();
        }
        //setDecorFlag();
        Utils.getSystemLanguge(this);
    }

    private void setDecorFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option;
            if (BuildConfig.LARGE_UI_SWITCH) {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            } else {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }

            decorView.setSystemUiVisibility(option);
            if (!BuildConfig.LARGE_UI_SWITCH) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            //4.4到5.0
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDecorFlag();
    }

    private void initLargeUIData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Resources resources = getApplication().getResources();
                ItemUtil itemUtil = new ItemUtil(App.getInstance());
                //itemUtil.deleteAll();
                int itemSize = itemUtil.getSize();
                Logger.d(TAG, "initLargeUIData, aaa itemSize: " + itemSize);
                if (itemSize == 0) {
                    String[] default_add_app_name = resources.getStringArray(R.array.default_add_app_name);
                    List<LargeUIItem> itemList = new ArrayList<>();
                    int cellX;
                    int cellY;
                    int screen;
                    for (int i = 0; i < Constants.DEFAULT_ADD_APP_PACKAGE_NAME.length; i++) {
                        if (i < Constants.PAGE_APP_MAX_COUNT) {
                            cellX = i / 2;
                        } else {
                            cellX = (i - Constants.PAGE_APP_MAX_COUNT) / 2;
                        }
                        cellY = i % 2;
                        screen = i / Constants.PAGE_APP_MAX_COUNT;
                        LargeUIItem item0 = new LargeUIItem();
                        item0.setType(i);
                        item0.setItemName(default_add_app_name[i]);
                        item0.setScreen(screen);
                        item0.setCellX(cellX);
                        item0.setCellY(cellY);
                        item0.setIsMove(false);
                        item0.setIsModify(false);
                        //Logger.d(TAG, "initLargeUIData, name: " + default_add_app_name[i] + ", screen: " + screen + "(" + cellX + "," + cellY + ")");
                        String pkgName = Constants.DEFAULT_ADD_APP_PACKAGE_NAME[i];
                        Logger.d(TAG, "initLargeUIData, pkgName: " + pkgName);
                        item0.setPackageName(pkgName);
                        if (!TextUtils.isEmpty(pkgName)) {
                            item0.setIsApp(true);
                        } else {
                            item0.setIsApp(false);
                        }
                        itemList.add(item0);
                    }
                    itemUtil.insertMulti(itemList);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            updateView_LargeUI();
                        }
                    });
                }
//                LargeUIItem chatItemParent = itemUtil.getItem(Constants.ITEM_TYPE_CHAT);
//                if (chatItemParent != null) {
//                    int containerSize = itemUtil.getContainerItemSize(chatItemParent.getId());
//                    if (containerSize == 0) {
//                        SharedPreferences.Editor editor = getSharedPreferences("video_call",MODE_PRIVATE).edit();
//
//                        List<LargeUIItem> chatItemList = new ArrayList<>();
//                        for (int i = 0; i < Constants.DEFAULT_CHAT_APPS_PACKAGE_NAME.length; i++) {
//                            LargeUIItem chatItem = new LargeUIItem();
//                            chatItem.setContainer(chatItemParent.getId());
//                            chatItem.setIsMove(false);
//                            chatItem.setIsModify(false);
//                            String pkgName = Constants.DEFAULT_CHAT_APPS_PACKAGE_NAME[i];
//                            chatItem.setPackageName(pkgName);
//                            if (!TextUtils.isEmpty(pkgName)) {
//                                chatItem.setIsApp(true);
//                            } else {
//                                chatItem.setIsApp(false);
//                            }
//                            chatItemList.add(chatItem);
//                        }
//                        //itemUtil.insertMulti(chatItemList);
//                   }
//
//               }
            }
        }).start();
    }

    private void initView_LargeUI() {
        initLargeUIData();
        TimeWeatherFragment timeWeatherFragment = new TimeWeatherFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.time_weather_view, timeWeatherFragment).commit();
        getSupportFragmentManager().beginTransaction().show(timeWeatherFragment);

        viewPager = findViewById(R.id.vp_horizontal_ntb);
        adapterLargeUI = new MainPagerAdapterLargeUI(getSupportFragmentManager(), fragments_LargeUI);
        viewPager.setAdapter(adapterLargeUI);
        updateView_LargeUI();
        viewPager.setOffscreenPageLimit(1);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        if (fragments_LargeUI == null) {
            fragments_LargeUI = new ArrayList<>();
        }

    }

    private void updateView_LargeUI() {
        ItemUtil itemUtil = new ItemUtil(this);
        mCurrAppCnt = itemUtil.getAllItems().size();
        Logger.d(TAG, "updateView_LargeUI, mCurrAppCnt: " + mCurrAppCnt + " itemUtil.getAllItems() size" + itemUtil.getAllItems().size());
        /*LargeUIFragment largeUIFragment = LargeUIFragment.newInstance();
        largeUIFragment.setmCurrPage(0);
        fragments_LargeUI.add(largeUIFragment);
        if (mCurrAppCnt > LargeUIFragment.FIRST_PAGE_APP_CNT) {
            int mod = mCurrAppCnt % (LargeUIFragment.FIRST_PAGE_APP_CNT + 2);
            int pagePlus = 0;
            if (mod >= LargeUIFragment.FIRST_PAGE_APP_CNT) {
                pagePlus = 1;
            }
            int pageCnt = mCurrAppCnt / (LargeUIFragment.FIRST_PAGE_APP_CNT + 2) + 1 + pagePlus;
            for (int i = 1; i < pageCnt; i++ ) {
                LargeUISecondFragment secondFragment = new LargeUISecondFragment();
                secondFragment.setmCurrPage(i);
                fragments_LargeUI.add(secondFragment);
            }
        }*/
        if (fragments_LargeUI != null && fragments_LargeUI.size() != 0) {
            fragments_LargeUI.clear();
        }

        int pageCnt = mCurrAppCnt / Constants.PAGE_APP_MAX_COUNT + 1;
        for (int i = 0; i < pageCnt; i++) {
            LargeUISecondFragment secondFragment = LargeUISecondFragment.newInstance(i);
            //secondFragment.setmCurrPage(i);
            fragments_LargeUI.add(secondFragment);
        }
        Log.d(TAG, " updateView_LargeUI  should show size " + fragments_LargeUI.size());
        adapterLargeUI.updateFragmentList(fragments_LargeUI);
    }

    @Override
    protected void initData() {
        registerReceiver();
    }

    public void player(String str) {
        if (getAIDLServer() != null) {
            try {
                getAIDLServer().play(str);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private ScheduleReceiver scheduleReceiver;
    private LargeUIItemPageReceiver largeUIItemPageReceiver;
    private ConnectionChangeReceiver connectionChangeReceiver;
    private BootReceiver mAppBroadcastReceiver;
    private LocaleChangeReceiver mLocaleChangeReceiver;

    protected void registerReceiver() {
        Log.d("MainActivity", "registerReceiver");
        scheduleReceiver = new ScheduleReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SCHEDULE_NOTIFY);
        registerReceiver(scheduleReceiver, filter);

        if (BuildConfig.LARGE_UI_SWITCH) {
            largeUIItemPageReceiver = new LargeUIItemPageReceiver();
            IntentFilter filter1 = new IntentFilter();
            filter1.addAction(Constants.APP_ITEM_COUNT_PAGE_CHANGED_ACTION);
            registerReceiver(largeUIItemPageReceiver, filter1);

            connectionChangeReceiver = new ConnectionChangeReceiver();
            IntentFilter filter2 = new IntentFilter();
            filter2.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(connectionChangeReceiver, filter2);

            mAppBroadcastReceiver = new BootReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            intentFilter.addDataScheme("package");
            registerReceiver(mAppBroadcastReceiver, intentFilter);

            mLocaleChangeReceiver = new LocaleChangeReceiver();
            IntentFilter localeFilter = new IntentFilter();
            localeFilter.addAction(Intent.ACTION_LOCALE_CHANGED);
            registerReceiver(mLocaleChangeReceiver, localeFilter);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(scheduleReceiver);
        if (BuildConfig.LARGE_UI_SWITCH) {
            unregisterReceiver(largeUIItemPageReceiver);
            unregisterReceiver(connectionChangeReceiver);
            unregisterReceiver(mAppBroadcastReceiver);
            unregisterReceiver(mLocaleChangeReceiver);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public static final String ACTION_SCHEDULE_NOTIFY = "com.vtech.schedule.notify";

    public static final String ACTION_SCHEDULE_SERVICE_NOTIFY = "com.vtech.schedule.service.notify";

    public class ScheduleReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "ScheduleReceiver is start! ----------------- action : " + intent.getAction());
            if (intent.getAction().equals(ACTION_SCHEDULE_NOTIFY)) {
                if (mFragments[1] != null && mFragments[1] instanceof MainFragment) {
                    MainFragment fragment = (MainFragment) mFragments[1];
                    fragment.refreshScheduleList();
                }
            }
            if (intent.getAction().equals(ACTION_SCHEDULE_SERVICE_NOTIFY)) {
                Intent i = new Intent();
                i.setComponent(new ComponentName("com.vtech.schedule", "com.vtech.schedule.service.AlarmService"));
                startService(i);
                Log.i(TAG, "AlarmService is start! ");
            }
        }
    }

    private class LargeUIItemPageReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "LargeUIItemPageReceiver action : " + intent.getAction());
            if (intent.getAction().equals(Constants.APP_ITEM_COUNT_PAGE_CHANGED_ACTION)) {
                updateView_LargeUI();
                if (intent.hasExtra("screen_page")) {
                    int screen_page = intent.getIntExtra("screen_page", 0);
                    Log.d(TAG, "LargeUIItemPageReceiver  跳转到指定页卡 : " + screen_page);
                    viewPager.setCurrentItem(screen_page, true);
                }

            }
        }
    }

    private class ConnectionChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "ConnectionChangeReceiver action : " + intent.getAction());
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                if (TextUtils.isEmpty(App.getInstance().getToken())) {
                    APIService.getInstance().registDevice(context);
                }
            }
        }
    }
}
