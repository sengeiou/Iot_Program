package com.vtech.vhealth.function.base;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

/**
 * View delegate base class
 * 视图层代理的基类
 */
public abstract class AppDelegate implements IDelegate {
    protected final SparseArray<View> mViews = new SparseArray<View>();

    protected View rootView;

    public abstract int getRootLayoutId();

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int rootLayoutId = getRootLayoutId();
        rootView = inflater.inflate(rootLayoutId, container, false);
    }

    @Override
    public int getOptionsMenuId() {
        return 0;
    }

    public Toolbar getToolbar() {
        return null;
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    public void setRootView(View rootView) {
        this.rootView = rootView;
    }

    @Override
    public void initWidget() {
    }

    public <T extends View> T bindView(int id) {
        T view = (T) mViews.get(id);
        if (view == null) {
            view = (T) rootView.findViewById(id);
            mViews.put(id, view);
        }
        return view;
    }

    public <T extends View> T get(int id) {
        return (T) bindView(id);
    }
    public <T extends TextView> T getTextView(int id) {
        return (T) bindView(id);
    }

    public void setText(int id, String txt) {
        TextView tv = get(id);
        if (tv != null) {
            tv.setText(txt);
        }
    }

    public void setOnClickListener(View.OnClickListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnClickListener(listener);
        }
    }

    public void setOnTouchListener(View.OnTouchListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnTouchListener(listener);
        }
    }

    public void setOnLongClickListener(View.OnLongClickListener listener, int... ids) {
        if (ids == null) {
            return;
        }
        for (int id : ids) {
            get(id).setOnLongClickListener(listener);
        }
    }

    public void toastShort(CharSequence msg) {
        Toast.makeText(rootView.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void toastLong(CharSequence msg) {
        Toast.makeText(rootView.getContext(), msg, Toast.LENGTH_LONG).show();
    }

    public <T extends Activity> T getActivity() {
        return (T) rootView.getContext();
    }
}
