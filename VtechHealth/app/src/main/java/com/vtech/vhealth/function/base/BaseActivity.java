package com.vtech.vhealth.function.base;

import android.os.Bundle;

import com.vtech.vhealth.R;

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
