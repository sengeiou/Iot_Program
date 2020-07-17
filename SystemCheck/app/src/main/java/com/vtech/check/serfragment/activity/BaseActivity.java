package com.vtech.check.serfragment.activity;

import android.os.Bundle;

import com.vtech.check.R;
import com.vtech.check.serfragment.AppDelegate;


/**
 * Presenter base class for Activity
 * Presenter层的实现基类
 */
public abstract class BaseActivity<T extends AppDelegate> extends ActivityPresenter<T> {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
    }
}
