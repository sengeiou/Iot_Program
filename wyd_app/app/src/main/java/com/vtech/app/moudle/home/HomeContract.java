package com.vtech.app.moudle.home;

import android.content.Context;

import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.moudle.BasePresenter;
import com.vtech.app.moudle.BaseView;

import java.util.List;

public interface HomeContract {
    interface View extends BaseView<Presenter> {
        void updateAppList(List<AppInfo> mDataList);

    }

    interface Presenter extends BasePresenter {
        void getAppList(Context context);
    }
}
