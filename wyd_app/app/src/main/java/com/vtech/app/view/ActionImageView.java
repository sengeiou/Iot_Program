package com.vtech.app.view;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.vtech.app.BuildConfig;

public class ActionImageView extends AppCompatImageView {

    public ActionImageView(Context context) {
        super(context);
    }

    public ActionImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!BuildConfig.LARGE_UI_SWITCH) {
                    setAlpha(0.5f);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (!BuildConfig.LARGE_UI_SWITCH) {
                    setAlpha(1f);
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //请求所有父控件不要拦截Touch事件
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }
}
