package com.vtech.app.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vtech.app.R;

import java.util.ArrayList;
import java.util.List;

public class CostAdapter extends BaseAdapter {

    private Context context;
    private List mDataList = new ArrayList<>();

    public CostAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
//        return mDataList.size();
        return 5;
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if (view == null || view.getTag() == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_cost, null);
            holder = new ViewHolder();
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
    }
}
