package com.vtech.app.moudle.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.vtech.app.util.Logger;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class GpsLocationStrategy implements LocationStrategy {
    public final static String TAG = "GpsLocationStrategy";

    private Context context;

    private LocationManager mLocationManager;

    private float mMinDistance = 0;

    private int mGpsCount;

    private UpdateLocationListener listener;


    public GpsLocationStrategy(Context context) {
        this.context = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void setListener(UpdateLocationListener listener) {
        this.listener = listener;
    }

    @Override
    public void requestLocation() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Logger.e(TAG,"no permission");
            return;
        }
        mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, mMinDistance, mGPSLocationListener);
        mLocationManager.addGpsStatusListener(mGpsStatusCallback);
    }

    @Override
    public void stopLocation() {
        mLocationManager.removeUpdates(mGPSLocationListener);
        mLocationManager.removeGpsStatusListener(mGpsStatusCallback);
    }

    LocationListener mGPSLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            Logger.i(TAG, "GPSLocationListener " +"，location ：经    度：" + location.getLongitude() + " 纬    度：" + location.getLatitude());
            //当GPS位置发生改变时，更新位置
            if (listener != null) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                StringBuilder sb = new StringBuilder();
                sb.append("GPS定位成功" + "\n");
                sb.append("定位类型: GPS" + "\n");
                sb.append("经度：");
                sb.append(location.getLongitude());
                sb.append("\n纬度：");
                sb.append(location.getLatitude());
                sb.append("\n高度：");
                sb.append(location.getAltitude());
                sb.append("\n速度：");
                sb.append(location.getSpeed());
                sb.append("\n时间：");
                sb.append(location.getTime());
                sb.append("\n精度：");
                sb.append(location.getAccuracy());
                sb.append("\n方位：");
                sb.append(location.getBearing());
                sb.append("\n时间：");
                sb.append(simpleDateFormat.format(new Date(location.getTime())));
                sb.append("\n星数：");
                sb.append(mGpsCount);
//                T.showShort(context, "currentLocation：" + sb.toString());
                if (listener != null) {
                    listener.updateLocationChanged(location,mGpsCount);
                }
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Logger.i(TAG, "GPSLocationListener onStatusChanged：AVAILABLE " + status);
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Logger.i(TAG, "GPSLocationListener onStatusChanged：OUT_OF_SERVICE " + status);
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Logger.i(TAG, "GPSLocationListener onStatusChanged：TEMPORARILY_UNAVAILABLE " + status);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Logger.i(TAG, "GPSLocationListener onProviderEnabled GPS开启了");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Logger.i(TAG, "GPSLocationListener onProviderDisabled GPS关闭了");
        }
    };

    private GpsStatus.Listener mGpsStatusCallback = new GpsStatus.Listener() {
        @SuppressLint("MissingPermission")
        public void onGpsStatusChanged(int event) { // GPS状态变化时的回调，如卫星数
            LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            GpsStatus status = locationManager.getGpsStatus(null); //取当前状态
            updateGpsStatus(event, status);
        }
    };

    private List<GpsSatellite> numSatelliteList = new ArrayList<>();


    private void updateGpsStatus(int event, GpsStatus status) {
        if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {
            int maxSatellites = status.getMaxSatellites();
            Iterator<GpsSatellite> it = status.getSatellites().iterator();
            numSatelliteList.clear();
            int count = 0;
            while (it.hasNext() && count <= maxSatellites) {
                GpsSatellite s = it.next();
                if (s.getSnr() != 0)//只有信躁比不为0的时候才算搜到了星
                {
                    numSatelliteList.add(s);
                    count++;
                }
            }
            mGpsCount = numSatelliteList.size();
//            changeLocationMode();
            Logger.i(TAG, "updateGpsStatus mGpsCount：" + mGpsCount);
        }
    }
}
