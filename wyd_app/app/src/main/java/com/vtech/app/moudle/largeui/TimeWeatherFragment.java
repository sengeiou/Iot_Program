package com.vtech.app.moudle.largeui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vtech.app.App;
import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.data.bean.WeatherBean;
import com.vtech.app.moudle.BaseActivity;
import com.vtech.app.moudle.MainActivity;

import java.util.List;
import butterknife.ButterKnife;

public class TimeWeatherFragment extends Fragment implements LargeUIContract.View, View.OnClickListener {
    private static final String TAG = "TimeWeatherFragment";
    private boolean isEn = true;
    private RelativeLayout dateLayout;
    private TextView apmTv;
    private TextView timeTv;
    private TextView dateTv;
    private TextView weekTv;
    private TextView  cityTv;
    private RelativeLayout weatherLayout;
    private ImageView weatherIv;
    private TextView  temperatureTv;
    private LinearLayout descripLayout;
    private TextView  descripTvOne;
    private TextView  descripTvTwo;
    private TextView  airTv;
    private LargeUIContract.Presenter mPresenter;
    private Handler handler;
    private Runnable runnable;
    private ConChangeReceiver conChangeReceiver;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view;
        if (App.getInstance().getLang() == 2) {
            isEn = true;
            view = inflater.inflate(R.layout.fragment_time_weather_en, container, false);
        } else {
            isEn = false;
            view = inflater.inflate(R.layout.fragment_my_time_weather_cn, container, false);
        }
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initView() {
        Log.d(TAG, "initView");
        if (!isEn) {
            cityTv = getActivity().findViewById(R.id.city_tv);
            weatherLayout = getActivity().findViewById(R.id.weather_layout);
            weatherIv = getActivity().findViewById(R.id.weather_icon);
            temperatureTv = getActivity().findViewById(R.id.temperature_tv);
            descripLayout = getActivity().findViewById(R.id.descrip_layout);
            descripTvOne = getActivity().findViewById(R.id.descrip_tv1);
            descripTvTwo = getActivity().findViewById(R.id.descrip_tv2);
            airTv = getActivity().findViewById(R.id.air_tv);
            dateLayout = getActivity().findViewById(R.id.date_layout);
            dateLayout.setOnClickListener(this);
            weatherLayout.setOnClickListener(this);
            temperatureTv.setOnClickListener(this);
            airTv.setOnClickListener(this);
            descripLayout.setOnClickListener(this);
        }
        apmTv = getActivity().findViewById(R.id.time_format_text);
        timeTv = getActivity().findViewById(R.id.time_tv);
        dateTv = getActivity().findViewById(R.id.date_tv);
        weekTv = getActivity().findViewById(R.id.week_tv);
    }

    private void initData() {
        if (!isEn) {
            conChangeReceiver = new ConChangeReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            getContext().registerReceiver(conChangeReceiver, filter);

            new LargeUIPresenter(this, getActivity());
            handler = new Handler();
        }
    }

    @Override
    public void updateWeather(WeatherBean bean) {
        if(bean != null) {
            if(bean.getData() != null) {
                cityTv.setText(bean.getData().getCity());
                String weatherIconStr = bean.getData().getType();
                if (weatherIconStr.contains("晴")) {
                    weatherIv.setImageResource(R.mipmap.icon_sun);
                } else if (weatherIconStr.contains("阴")) {
                    weatherIv.setImageResource(R.mipmap.icon_yintian);
                } else if (weatherIconStr.contains("多云")) {
                    weatherIv.setImageResource(R.mipmap.icon_duoyun);
                } else if (weatherIconStr.contains("小雨")) {
                    weatherIv.setImageResource(R.mipmap.icon_yu);
                } else if (weatherIconStr.contains("阵雨")) {
                    weatherIv.setImageResource(R.mipmap.icon_qingyu);
                } else if (weatherIconStr.contains("雷阵雨")) {
                    weatherIv.setImageResource(R.mipmap.icon_leizhenyu);
                } else if (weatherIconStr.contains("雨")) {
                    weatherIv.setImageResource(R.mipmap.icon_yu);
                } else if (weatherIconStr.contains("雪")) {
                    weatherIv.setImageResource(R.mipmap.icon_xue);
                } else if (weatherIconStr.contains("雾")) {
                    weatherIv.setImageResource(R.mipmap.icon_wu);
                }
                temperatureTv.setText(bean.getData().getWendu() + "°C");
                descripTvOne.setText(bean.getData().getType());
                descripTvTwo.setText(" " + bean.getData().getLow() +
                        "/" +bean.getData().getHigh());
                airTv.setText("空气质量 " + bean.getData().getQuality());
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.date_layout:
                if(!isEn) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(dateTv.getText().toString().trim());
                    sb.append(weekTv.getText().toString().trim());
                    sb.append(timeTv.getText().toString().trim());
                    sb.append(apmTv.getText().toString().trim());
                    player(sb.toString());
                }
                break;
            case R.id.weather_layout:
            case R.id.temperature_tv:
            case R.id.descrip_layout:
            case R.id.air_tv:
                playerWeather();
                break;
            default:
                break;
        }
    }

    public void player(String str) {
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.player(str);
    }

    private void playerWeather() {
        StringBuilder sb = new StringBuilder();
        sb.append("天气情况"+descripTvOne.getText().toString().trim());
        String[] descStrs = descripTvTwo.getText().toString().trim().split("/");
        sb.append("最低气温"+descStrs[0]+",最高气温"+descStrs[1]);
        sb.append(airTv.getText().toString().trim());
        sb.append("当前气温"+temperatureTv.getText().toString().trim());
        player(sb.toString());
    }

    private class ConChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
               ConnectivityManager connectivityManager =
                       (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
               NetworkInfo info = connectivityManager.getActiveNetworkInfo();
               if(info != null && info.isAvailable()) {
                   if(runnable != null){
                       handler.removeCallbacks(runnable);
                   }
                   runnable = new Runnable() {
                       @Override
                       public void run() {
                           try {
                               BaseActivity activity = (BaseActivity) getActivity();
                               double latitu = activity.getAIDLServer().get_latitude();
                               double longitu = activity.getAIDLServer().get_longitude();
                               mPresenter.getWeather(latitu,longitu);
                               handler.postDelayed(this, 15 * 60 * 1000);// 15分钟后执行this，即runable
                           } catch (RemoteException e) {
                               e.printStackTrace();
                           }
                       }
                   };
                   handler.postDelayed(runnable, 100);
               }
            }
        }
    }

    @Override
    public void updateAppList(List<AppInfo> mDataList) {

    }

    @Override
    public void setPresenter(LargeUIContract.Presenter presenter) {
       mPresenter = presenter;
    }

    @Override
    public void updateLocation() {

    }

    public static TimeWeatherFragment newInstance() {
        TimeWeatherFragment fragment = TimeWeatherFragment.newInstance();
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(runnable != null) {
            handler.removeCallbacks(runnable);
        }
        if(conChangeReceiver != null) {
            getContext().unregisterReceiver(conChangeReceiver);
        }
    }
}
