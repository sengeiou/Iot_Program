package com.vtech.check.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.vtech.check.R;
import com.vtech.check.view.DrawLineView;
import com.vtech.check.view.MultiTouchView;

public class TouchTest extends BaseTest{

    View rootview;
    /*MultiTouchView multiTouchView;*/
    DrawLineView dwView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            rootview=inflater.inflate(R.layout.touch,container,false);
        initView();
        return rootview;
    }

    private void initView() {
/*
        multiTouchView = (MultiTouchView) rootview.findViewById(R.id.multiTouchView);
        multiTouchView.setOnMultiTouchListener(new MultiTouchView.OnMultiTouchListener());*/

        dwView=(DrawLineView)rootview.findViewById(R.id.dwView);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.touch_title);
    }


    @Override
    public boolean isNeedTest() {
        return  getSystemProperties("touch", true);
    }
}
