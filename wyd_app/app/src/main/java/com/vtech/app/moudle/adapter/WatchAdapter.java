package com.vtech.app.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtech.app.R;
import com.vtech.app.data.bean.HomeProtectZoneStatusBean;

import java.util.ArrayList;
import java.util.List;


public class WatchAdapter extends BaseAdapter {

    private Context context;
    private List<HomeProtectZoneStatusBean> mDataList = new ArrayList<>();

    public WatchAdapter(Context context, List<HomeProtectZoneStatusBean> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public void setDataList(List<HomeProtectZoneStatusBean> mDataList) {
        this.mDataList = mDataList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDataList.size();
//        return 4;
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
            view = LayoutInflater.from(context).inflate(R.layout.item_watch, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        /*if(i == 1){
            holder.status.setText("房屋正门未关闭");
            holder.status.setTextColor(context.getResources().getColor(R.color.circle_color));
            holder.descrip.setText("Door magnet");
            holder.icon.setImageResource(R.mipmap.icon_menci);
            holder.statusIcon.setImageResource(R.mipmap.icon_warning);
        }else if(i == 2){
            holder.status.setText("厨房灶台状态—正常");
            holder.status.setTextColor(context.getResources().getColor(R.color.safe_status_title));
            holder.descrip.setText("Temperature Sensor");
            holder.icon.setImageResource(R.mipmap.icon_kitchen);
            holder.statusIcon.setImageResource(R.mipmap.icon_normal);
        }else if(i == 3){
            holder.status.setText("室内窗户防坠落—正常");
            holder.status.setTextColor(context.getResources().getColor(R.color.safe_status_title));
            holder.descrip.setText("Infrared sensor");
            holder.icon.setImageResource(R.mipmap.icon_window);
            holder.statusIcon.setImageResource(R.mipmap.icon_normal);
        }*/

        if (mDataList != null && mDataList.size() > 0) {
            HomeProtectZoneStatusBean bean = mDataList.get(i);
            holder.status.setText(bean.getZone() + bean.getPlace() + (bean.isNormal() ? "-正常" : "-不正常"));
            holder.status.setTextColor(bean.isNormal() ? context.getResources().getColor(R.color.safe_status_title) : context.getResources().getColor(R.color.circle_color));
            holder.descrip.setText(bean.getProbe());
            if("大门".equals(bean.getZone())){
                holder.icon.setImageResource(R.mipmap.icon_menci);
            }else if("厨房".equals(bean.getZone())){
                holder.icon.setImageResource(R.mipmap.icon_kitchen);
            }else if("室内".equals(bean.getZone())){
                holder.icon.setImageResource(R.mipmap.icon_window);
            }
            holder.statusIcon.setImageResource(bean.isNormal() ? R.mipmap.icon_normal:R.mipmap.icon_warning);
        }
        return view;
    }


    class ViewHolder {

        ImageView icon;

        TextView status;

        TextView descrip;

        ImageView statusIcon;

        ViewHolder(View view) {
            icon = view.findViewById(R.id.icon);

            status = view.findViewById(R.id.status);

            descrip = view.findViewById(R.id.descrip);

            statusIcon = view.findViewById(R.id.status_icon);
        }
    }
}
