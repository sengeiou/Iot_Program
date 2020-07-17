package com.vtech.app.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vtech.app.R;
import com.vtech.app.data.bean.Schedule;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SchedleAdapter extends BaseAdapter {
    private Context context;
    private List<Schedule> mDataList = new ArrayList<>();

    public SchedleAdapter(Context context) {
        this.context = context;
    }

    public SchedleAdapter(Context context, List<Schedule> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public void setDataList(List<Schedule> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
//        return 3;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_schedle, null);
            holder = new ViewHolder();
            holder.textView = view.findViewById(R.id.textView);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mDataList != null && mDataList.size() > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("KK:mm a", Locale.ENGLISH);
            Schedule bean = mDataList.get(i);
            Date date = new Date(bean.startTime);
            holder.textView.setText(sdf.format(date) +"—"+bean.title + bean.remark);
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
    }
}
