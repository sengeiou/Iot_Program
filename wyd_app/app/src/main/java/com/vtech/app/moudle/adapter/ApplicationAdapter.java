package com.vtech.app.moudle.adapter;

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
import com.vtech.app.data.bean.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.VH> {

    Context context;

    List<AppInfo> mDataList = new ArrayList<>();

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
    
    public void setDataList(List<AppInfo> dataList) {
        this.mDataList = dataList;
        this.notifyDataSetChanged();
    }

    public ApplicationAdapter(Context context) {
        this.context = context;
    }

    public ApplicationAdapter(Context context, List<AppInfo> list) {
        this.context = context;
        this.mDataList = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_application, viewGroup, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH vh, final int i) {
        if (mDataList.size() > 0) {
            vh.icon.setImageDrawable(mDataList.get(i).getDrawable());
            vh.name.setText(mDataList.get(i).getAppName());

            vh.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = context.getPackageManager().getLaunchIntentForPackage(mDataList.get(i).getPackageName());
                    context.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }
}
