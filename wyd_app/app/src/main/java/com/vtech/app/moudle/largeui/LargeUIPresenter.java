package com.vtech.app.moudle.largeui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.vtech.app.R;
import com.vtech.app.data.APIService;
import com.vtech.app.data.Constant;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.data.bean.RegeoInfo;
import com.vtech.app.data.bean.WeatherBean;
import com.vtech.app.moudle.MainActivity;
import com.vtech.app.util.Logger;
import com.vtech.app.util.OkHttpUtil;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LargeUIPresenter implements LargeUIContract.Presenter {

    public final static String TAG = "LargeUIPresenter";
    private LargeUIContract.View view;
    private Context context;

    public LargeUIPresenter(LargeUIContract.View view, Context context) {
        this.view = view;
        this.context = context;
        view.setPresenter(this);
    }

    @Override
    public void getLocation() {
        MainActivity activity = (MainActivity) context;
        if (activity.getAIDLServer() != null) {
            try {
                final double latitude = activity.getAIDLServer().get_latitude();
                final double longtitude = activity.getAIDLServer().get_longitude();
                Log.i(TAG, "latitude : " + latitude + " , longtitude : " + longtitude);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.i(TAG, "getLocation thread");
                        final RequestBody formBody = new FormBody.Builder()
                                .add("version", "v6")
                                .add("version", "v6")
                                .build();
                        /*List<Address> addList = new ArrayList<>();
                        Geocoder geocoder = new Geocoder(App.getInstance());
                        try {
                            addList = geocoder.getFromLocation(40, 116, 1);
                        } catch (IOException e) {
                            Log.e(TAG, "getFromLocation: got IOException: ", e);
                        }
                        if (addList != null) {
                            Log.i(TAG, "getLocation thread, size: " + addList.size());
                        }
                        if (addList != null && addList.size() > 0) {
                            for (int i = 0; i < addList.size(); i++) {
                                Address ad = addList.get(i);
                                Log.i(TAG, "cityname: " + ad.getLocality());
                            }
                        }*/
                    }
                }).start();

            } catch (RemoteException e) {
                Log.e(TAG, "get POS: got RemoteException", e);
            }
        } else {
            Log.e(TAG, "getLocation, getAIDLServer is null ");
        }
    }

    @Override
    public void getWeather(double latitude,double longtitude) {
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

    @Override
    public void setSafeBroadcast(int status) {
        //Intent intent = new Intent("com.vtech.app.deploy_status_notify");
        //intent.putExtra("DEPLOY_STATUS", status);
        //intent.setFlags(0x01000000);//解决广播发送不成功的问题
        //context.sendBroadcast(intent);
    }

    @Override
    public void getFixedAppList(Context context) {
        List<AppInfo> dataList = getAllListApp(context);
        List<AppInfo> fixedAppList = new ArrayList<>();

        view.updateAppList(fixedAppList);
    }

    @Override
    public void getAppList(Context context) {
        List<AppInfo> dataList = getAllListApp(context);

        view.updateAppList(dataList);
    }

    private List<AppInfo> getAllListApp(Context context) {
        List<AppInfo> mDataList = new ArrayList<>();

        final PackageManager packageManager = context.getPackageManager();
        final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        // get all apps
        final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
        for (int i = 0; i < apps.size(); i++) {
            String packageName = apps.get(i).activityInfo.packageName;
            Log.i(TAG, "all getAppProcessName: " + packageName + " , AppName : " + apps.get(i).activityInfo.loadLabel(packageManager).toString());
            if (packageName.contains("com.android.email")
                    || packageName.contains("com.sohu.inputmethod.sogou")
                    || packageName.contains("com.vtech.voiceassistant")
                    || packageName.contains("com.android.quicksearchbox")
                    || packageName.contains("org.chromium.webview_shell")
                    || packageName.contains("com.softwinner.fireplayer")
                    || packageName.contains("com.iflytek.inputmethod")
                    || packageName.contains("com.android.dialer")
                    || packageName.contains("com.android.messaging")
                    || packageName.contains("com.android.settings")
                    || packageName.contains("com.vtech.vhealth")
                    || packageName.contains("com.vtech.homesecurity")
                    || packageName.contains("com.whatsapp")
                    || packageName.contains("com.tencent.mm")
                    || packageName.contains("com.vtech.app") //移除自身
                    || packageName.contains("com.android.contacts")//移除联系人
                    || packageName.contains("com.vtech.schedule")
                    || packageName.contains("com.vtech.face")) {
                continue;
            }
            AppInfo bean = new AppInfo();
            bean.setAppName(apps.get(i).activityInfo.loadLabel(packageManager).toString());
            bean.setPackageName(packageName);
            if (packageName.contains("com.android.settings")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_setting));
            } else if (packageName.contains("com.android.camera2")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_camera));
            } else if (packageName.contains("com.softwinner.update")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_update));
            } else if (packageName.contains("com.android.gallery3d")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_gallery));
            } else if (packageName.contains("com.android.contacts")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_contact));
            } else if (packageName.contains("com.android.deskclock")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_deskclock));
            } else if (packageName.contains("com.android.messaging")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_messaging));
            } else if (packageName.contains("com.android.calculator2")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_calculator));
            } else if (packageName.contains("com.android.soundrecorder")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_soundrecorder));
            } else if (packageName.contains("com.android.documentsui")) {
                bean.setDrawable(context.getResources().getDrawable(R.mipmap.ic_home_documentsui));
            } else {
                bean.setDrawable(apps.get(i).activityInfo.loadIcon(packageManager));
            }
            mDataList.add(bean);
            //Log.i(TAG, "app list size = " + mDataList.size());
        }
        return mDataList;
    }
}
