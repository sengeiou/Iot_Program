package com.vtech.vhealth.function.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatButton;

public class ActionButton extends AppCompatButton {

    public ActionButton(Context context) {
        super(context);
    }

    public ActionButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                    setAlpha(0.5f);
                break;
            case MotionEvent.ACTION_UP:
                    setAlpha(1f);
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
