package com.vtech.app.moudle.largeui;

import android.content.Context;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.data.bean.WeatherBean;
import com.vtech.app.moudle.BasePresenter;
import com.vtech.app.moudle.BaseView;

import java.util.List;

public interface LargeUIContract {
    interface View extends BaseView<Presenter> {
        void updateWeather(WeatherBean bean);
        void updateAppList(List<AppInfo> mDataList);
        void updateLocation();
    }

    interface Presenter extends BasePresenter {
        void getWeather(double latitude,double longtitude);
        void setSafeBroadcast(int status);
        void getAppList(Context context);
        void getFixedAppList(Context context);
        void getLocation();
    }
}
