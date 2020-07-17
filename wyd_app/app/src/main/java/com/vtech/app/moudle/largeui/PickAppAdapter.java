package com.vtech.app.moudle.largeui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtech.app.R;
import com.vtech.app.data.Constant;
import com.vtech.app.data.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class PickAppAdapter extends RecyclerView.Adapter<PickAppAdapter.VH> {


    public static class VH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        View layout;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            layout = itemView.findViewById(R.id.item_layout);
        }
    }

    private Context mContext;
    List<AppInfo> mDataList = new ArrayList<>();

    public void setDataList(List<AppInfo> dataList) {
        this.mDataList = dataList;
        this.notifyDataSetChanged();
    }

    public PickAppAdapter(Context context) {
        this.mContext = context;
    }

    public PickAppAdapter(List<AppInfo> mDataList) {
        this.mDataList = mDataList;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pick_application, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        final AppInfo appInfo = mDataList.get(position);
        holder.icon.setImageDrawable(appInfo.getDrawable());
        holder.name.setText(appInfo.getAppName());
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickCallback != null) {
                    mClickCallback.setResult(appInfo);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    private ClickCallback mClickCallback;
    public void setCallback(ClickCallback callback) {
        this.mClickCallback = callback;
    }

    public interface ClickCallback {
        void setResult(AppInfo appInfo);
    }
}
