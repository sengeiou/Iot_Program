package com.vtech.app.moudle.largeui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.data.bean.WeatherBean;
import com.vtech.app.largeuidb.dbutils.ItemUtil;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;
import com.vtech.app.moudle.BaseFragment;
import com.vtech.app.moudle.adapter.ApplicationAdapterLarge;
import com.vtech.app.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class LargeUIFragment extends BaseFragment implements LargeUIContract.View {

    private static String TAG = "LargeUIFragment";
    public static final int FIRST_PAGE_APP_CNT = 6;
    RecyclerView mRecyclerView;

    ApplicationAdapterLarge mAdapter;

    Handler handler = new Handler();

    LargeUIContract.Presenter mPresenter;

    private int mCurrPage;

    private List<LargeUIItem> largeUIItemList = new ArrayList<>();

    public void setmCurrPage(int mCurrPage) {
        this.mCurrPage = mCurrPage;
    }

    public static LargeUIFragment newInstance() {
        LargeUIFragment fragment = new LargeUIFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_large_ui;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycle_view);
        // 竖直方向的网格样式，每行四个Item
        GridLayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(getActivity(), 2, OrientationHelper.VERTICAL, false);
        //mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        // mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.HORIZONTAL));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ApplicationAdapterLarge(getActivity(), mCurrPage,new ItemUtil(getContext()), largeUIItemList);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        new LargeUIPresenter(this, getActivity());
        registerReceiver();
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getFixedAppList(getContext());
            }
        },500);*/
        /*handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getWeather(LargeUIFragment.this);//刷新天气
            }
        },500);*/
    }

    @Override
    protected void onLazyLoad() {
    }

    @Override
    public void updateLocation() {

    }

    @Override
    public void updateWeather(WeatherBean bean) {
       /* if (bean.getCode() == 2) {
            String weatherStr = bean.getData().getType() + " " +
                    (bean.getData().getHigh().startsWith("高温") ? bean.getData().getHigh().substring(3) : bean.getData().getHigh()) + "/" +
                    (bean.getData().getLow().startsWith("低温") ? bean.getData().getLow().substring(3) : bean.getData().getLow());

            String weatherIconStr = bean.getData().getType();
            int weatherIconId = 0;
            if (weatherIconStr.startsWith("晴")) {
                weatherIconId = R.mipmap.icon_sun;
            } else if (weatherIconStr.startsWith("阴")) {
                weatherIconId = R.mipmap.icon_yintian;
            } else if (weatherIconStr.startsWith("多云")) {
                weatherIconId = R.mipmap.icon_duoyun;
            } else if (weatherIconStr.startsWith("小雨")) {
                weatherIconId = R.mipmap.icon_yu;
            } else if (weatherIconStr.startsWith("阵雨")) {
                weatherIconId = R.mipmap.icon_qingyu;
            } else if (weatherIconStr.startsWith("雷阵雨")) {
                weatherIconId = R.mipmap.icon_leizhenyu;
            } else if (weatherIconStr.contains("雪")) {
                weatherIconId = R.mipmap.icon_xue;
            } else if (weatherIconStr.contains("雾")) {
                weatherIconId = R.mipmap.icon_wu;
            }
            Logger.e(TAG, "getWeather weatherStr : " + weatherStr + ", weatherIconStr: " + weatherIconStr);
            mAdapter.setWeatherData(weatherStr, weatherIconId);
        } else {
            Logger.e(TAG, "getWeather fail info : " + bean.getMsg());
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //getContext().unregisterReceiver(myInstallReceiver);
        getContext().unregisterReceiver(weatherReceiver);
    }

    void registerReceiver() {
        /*myInstallReceiver = new MyInstallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // ya. 设置接收广播的类型
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        // 3. 动态注册：调用Context的registerReceiver（）方法
        getContext().registerReceiver(myInstallReceiver, intentFilter);
*/
        weatherReceiver = new WeatherReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        getContext().registerReceiver(weatherReceiver, filter);
    }

    //private MyInstallReceiver myInstallReceiver;
    private WeatherReceiver weatherReceiver;

    @Override
    public void updateAppList(List<AppInfo> dataList) {
        /*if (mAdapter != null && dataList != null) {
            if (BuildConfig.LARGE_UI_FIXED_APP) {
                mAdapter.setDataList(dataList);
            } else {
                if (dataList.size() <= FIRST_PAGE_APP_CNT) {
                    mAdapter.setDataList(dataList);
                } else {
                    mAdapter.setDataList(dataList.subList(0, FIRST_PAGE_APP_CNT));
                }
            }

        }*/
    }

    @Override
    public void setPresenter(LargeUIContract.Presenter presenter) {
        mPresenter = presenter;
    }

    private class MyInstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "HomeBroadcastReceiver action : " + intent.getAction());
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")
                    || intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")
                    || intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
                mPresenter.getAppList(getContext());
            }
        }
    }

    public static final String ACTION_ASSISTANT_PLAY_WEATHER = "com.vtech.voiceassistant.event.weather";

    public class WeatherReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "WeatherReceiver is start! ----------------- action : " + intent.getAction());

            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //mPresenter.getWeather(LargeUIFragment.this);//刷新天气
                    }
                }, 300);
            }

            /*if (intent.getAction().equals(ACTION_ASSISTANT_PLAY_WEATHER)) {
                playerWeather();
            }*/
        }
    }
}
