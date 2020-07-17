package com.vtech.app.moudle.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.moudle.BaseFragment;
import com.vtech.app.moudle.adapter.ApplicationAdapter;
import com.vtech.app.util.Logger;

import java.util.List;

public class HomeFragment extends BaseFragment implements HomeContract.View {
    RecyclerView mRecyclerView;

    ApplicationAdapter mAdapter;

    Handler handler = new Handler();

    HomeContract.Presenter mPresenter;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycle_view);
        // 竖直方向的网格样式，每行四个Item
        GridLayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 5, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ApplicationAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {
        new HomePresenter(this);
        registerReceiver();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPresenter.getAppList(getContext());
            }
        },2400);
    }

    @Override
    protected void onLazyLoad() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(myInstallReceiver);
    }

    void registerReceiver() {
        myInstallReceiver = new MyInstallReceiver();
        IntentFilter intentFilter = new IntentFilter();
        // ya. 设置接收广播的类型
        intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        intentFilter.addAction(Intent.ACTION_PACKAGE_REPLACED);
        intentFilter.addDataScheme("package");
        // 3. 动态注册：调用Context的registerReceiver（）方法
        getContext().registerReceiver(myInstallReceiver, intentFilter);
    }

    MyInstallReceiver myInstallReceiver;

    @Override
    public void updateAppList(List<AppInfo> mDataList) {
        mAdapter.setDataList(mDataList);
    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public class MyInstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG,"HomeBroadcastReceiver action : " + intent.getAction());
            if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")
                    || intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")
                    || intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
                mPresenter.getAppList(getContext());
            }
        }
    }
}
