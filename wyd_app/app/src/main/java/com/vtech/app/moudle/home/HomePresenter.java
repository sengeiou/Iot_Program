package com.vtech.app.moudle.home;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class HomePresenter implements HomeContract.Presenter{

    public final static String TAG = "HomePresenter";
    private HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
        view.setPresenter(this);
    }

    @Override
    public void getAppList(Context context) {
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
//                    || packageName.contains("com.android.calculator2")
                    || packageName.contains("com.android.quicksearchbox")
//                    || packageName.contains("com.android.soundrecorder")
                    || packageName.contains("org.chromium.webview_shell")
                    || packageName.contains("com.softwinner.fireplayer")
                    || packageName.contains("com.iflytek.inputmethod")
                    || packageName.contains("com.android.dialer")
                    || packageName.contains("com.vtech.vhealth")
                    || packageName.contains("com.vtech.homesecurity")
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
            Log.i(TAG, "app list size = " + mDataList.size());
            view.updateAppList(mDataList);
        }
    }
}
