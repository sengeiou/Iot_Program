package com.vtech.app.moudle.main;

import android.support.v4.app.Fragment;

import com.vtech.app.data.bean.WeatherBean;
import com.vtech.app.moudle.BasePresenter;
import com.vtech.app.moudle.BaseView;

public interface MainContract {
    interface View extends BaseView<Presenter> {
        void updateWeather(WeatherBean bean);
    }

    interface Presenter extends BasePresenter {
        void getWeather(Fragment fragment);

        void setSafeBroadcast(int status);
    }
}
