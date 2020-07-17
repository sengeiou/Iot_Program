package com.vtech.app.moudle.main;

import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.gson.Gson;
import com.vtech.app.data.APIService;
import com.vtech.app.data.Constant;
import com.vtech.app.data.bean.RegeoInfo;
import com.vtech.app.data.bean.WeatherBean;
import com.vtech.app.moudle.MainActivity;
import com.vtech.app.util.Logger;
import com.vtech.app.util.OkHttpUtil;

import okhttp3.Call;
import okhttp3.Response;

public class MainPresenter implements MainContract.Presenter{
    public final static String TAG = "MainPresenter";
    private MainContract.View view;
    private Context context;

    public MainPresenter(MainContract.View view, Context context) {
        this.view = view;
        this.context = context;
        view.setPresenter(this);
    }

    @Override
    public void getWeather(Fragment fragment) {
        MainActivity activity = (MainActivity) fragment.getActivity();
        if (activity.getAIDLServer() != null) {
            try {
                double latitude = activity.getAIDLServer().get_latitude();
                double longtitude = activity.getAIDLServer().get_longitude();
                Log.i(TAG, "latitude : " + latitude + " , longtitude : " + longtitude);

                if (latitude != 0f && longtitude != 0f) {
                    String url = Constant.URL_AMAP_REGEO + "?key=" + Constant.AMAP_KEY + "&location=" + longtitude + "," + latitude;
                    OkHttpUtil.get(url, new OkHttpUtil.SimpleResponseHandler() {
                        @Override
                        public void onSuccess(Call call, Response response) {
                            try {
                                Gson gson = new Gson();
                                RegeoInfo info = gson.fromJson(response.body().string(), RegeoInfo.class);
                                if (Constant.SUCCESS.equals(info.getStatus())) {
                                    String cityName = info.getRegeocode().getAddressComponent().getCity();
                                    getWeatherData(cityName);
                                } else {
                                    Logger.i(TAG, "getRegeo fail info : " + info.getInfo());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(Exception error) {
                            Logger.i(TAG, "getRegeo error info : " + error.getMessage());
                        }
                    });
                }
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.e(TAG, "getAIDLServer is null ");
        }
    }

    @Override
    public void setSafeBroadcast(int status) {
        Intent intent = new Intent("com.vtech.app.deploy_status_notify");
        intent.putExtra("DEPLOY_STATUS", status);
        intent.setFlags(0x01000000);//解决广播发送不成功的问题
        context.sendBroadcast(intent);
    }

    public void getWeatherData(String cityName){
        APIService.getInstance().getWether(cityName.replace("市",""), new OkHttpUtil.SimpleResponseHandler() {
            @Override
            public void onSuccess(Call call, Response response) {
                try {
                    Gson gson = new Gson();
                    WeatherBean bean = gson.fromJson(response.body().string(), WeatherBean.class);
                    view.updateWeather(bean);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception error) {
                Logger.e(TAG, "getWeather onFailure error info : " + error.getMessage());
            }
        });
    }
}
