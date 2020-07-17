package com.vtech.vhealth.function.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AutofitHeightViewPager extends ViewPager {
    public static final String POSITION = "position";

    private int mCurPosition;
    private int mHeight = 0;

    private HashMap<Integer, View> mChildrenViews = new LinkedHashMap<Integer, View>();

    private boolean scrollble = false;

    public AutofitHeightViewPager(Context context) {
        super(context);
    }

    public AutofitHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChildrenViews.size() > mCurPosition) {
            View child = mChildrenViews.get(mCurPosition);
            if (child != null) {
                child.measure(widthMeasureSpec, View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                mHeight = child.getMeasuredHeight();
            }
        }

        if (mHeight != 0) {
            heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(mHeight, View.MeasureSpec.EXACTLY);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void updateHeight(int current) {
        this.mCurPosition = current;
        if (mChildrenViews.size() > current) {
            ViewGroup.LayoutParams layoutParams = getLayoutParams();
            if (layoutParams == null) {
                layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, mHeight);
            } else {
                layoutParams.height = mHeight;
            }

            setLayoutParams(layoutParams);
        }
    }

    public void setViewPosition(View view, int position) {
        mChildrenViews.put(position, view);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (!scrollble) {
            return true;
        }
        return super.onTouchEvent(ev);
    }

    public boolean isScrollble() {
        return scrollble;
    }

    public void setScrollble(boolean scrollble) {
        this.scrollble = scrollble;
    }
}