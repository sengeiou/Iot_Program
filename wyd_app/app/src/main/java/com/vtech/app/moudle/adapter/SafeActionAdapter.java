package com.vtech.app.moudle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vtech.app.R;
import com.vtech.app.data.bean.HomeSecurityCombineZoneStatusBean;
import com.vtech.app.util.DateUtils;

import java.util.ArrayList;
import java.util.List;


public class SafeActionAdapter extends BaseAdapter {

    private Context context;
    private List<HomeSecurityCombineZoneStatusBean> mDataList = new ArrayList<>();

    public SafeActionAdapter(Context context, List<HomeSecurityCombineZoneStatusBean> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    public void setDataList(List<HomeSecurityCombineZoneStatusBean> mDataList) {
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
            view = LayoutInflater.from(context).inflate(R.layout.item_safe_action, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (mDataList != null && mDataList.size() > 0) {
            HomeSecurityCombineZoneStatusBean bean = mDataList.get(i);
            holder.timeTv.setText(DateUtils.timeStampDate(bean.getStartTime()) + " - " + DateUtils.timeStampDate(bean.getEndTime()));
            holder.locationTv.setText(bean.getZoneName());

            switch (bean.getTriggerStatus()) {
                case 0:
                    holder.status.setText(context.getString(R.string.not_protection));
                    break;
                case 1:
                    holder.status.setText(context.getString(R.string.wait_protection));
                    break;
                case 2:
                    holder.status.setText(context.getString(R.string.is_protection));
                    break;
                case 3:
                    holder.status.setText(context.getString(R.string.area_normal));
                    break;
                case 4:
                    holder.status.setText(context.getString(R.string.area_error));
                    break;
            }
            holder.actionTv.setText(bean.getTriggerStatus() == 4 ? context.getString(R.string.human_activity_detected) : context.getString(R.string.human_activity_undetected));
            holder.phoneStatus.setText(bean.getDialResultSuccess() == 0 ? context.getString(R.string.unreceived_call) : context.getString(R.string.received_call));
            holder.messageStatus.setText(bean.getSmsResultSuccess() == 0 ? context.getString(R.string.unreceived_message) : context.getString(R.string.received_message));
        }
        return view;
    }

    static
    class ViewHolder {

        TextView timeTv;

        TextView locationTv;

        TextView actionTv;

        TextView status;

        TextView phoneStatus;

        TextView messageStatus;

        ViewHolder(View view) {
            timeTv = view.findViewById(R.id.time_tv);
            locationTv = view.findViewById(R.id.location_tv);

            actionTv = view.findViewById(R.id.action_tv);
            status = view.findViewById(R.id.status);

            phoneStatus = view.findViewById(R.id.phone_status);
            messageStatus = view.findViewById(R.id.message_status);
        }
    }
}
