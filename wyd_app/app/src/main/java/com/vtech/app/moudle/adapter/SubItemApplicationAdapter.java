package com.vtech.app.moudle.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;
import com.vtech.app.moudle.largeui.SubItemActivity;

import java.util.ArrayList;
import java.util.List;

public class SubItemApplicationAdapter extends RecyclerView.Adapter<SubItemApplicationAdapter.VH> {

    Context context;
    List<AppInfo> mDataList = new ArrayList<>();
    private PackageManager pm;

    public SubItemApplicationAdapter(Context context, List<AppInfo> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
        pm = context.getPackageManager();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.sub_item_application_large_ui, viewGroup, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        AppInfo item = mDataList.get(i);
        try {
            final String pkgName = item.getPackageName();
            Log.d("SubItemAdapter", "pkgName; " + pkgName);
            if (!TextUtils.isEmpty(pkgName)) {
                ApplicationInfo appInfo = pm.getApplicationInfo(item.getPackageName(), PackageManager.GET_META_DATA);
                Log.d("SubItemAdapter "," appInfo = null ?" +(appInfo == null));
                vh.icon.setImageDrawable(pm.getApplicationIcon(appInfo));
                vh.name.setText(pm.getApplicationLabel(appInfo));
                vh.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context != null) {
                            Intent intent = context.getPackageManager().getLaunchIntentForPackage(pkgName);
                            context.startActivity(intent);
                        }
                    }
                });
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

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

}
