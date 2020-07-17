/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.fl.model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.vtech.face.R;

import java.util.ArrayList;
import java.util.List;

public class FaceAdapter extends RecyclerView.Adapter<FaceAdapter.VH> {

    Context context;

    List<FaceListResult.FaceBean> mDataList = new ArrayList<>();

    public class VH extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView name;
        View delete;

        public VH(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            icon = itemView.findViewById(R.id.icon);
            delete = itemView.findViewById(R.id.tv_delete);
        }
    }

    public void setDataList(List<FaceListResult.FaceBean> dataList) {
        this.mDataList = dataList;
        this.notifyDataSetChanged();
    }

    public List<FaceListResult.FaceBean> getDataList() {
        return mDataList;
    }

    public FaceAdapter(Context context) {
        this.context = context;
    }

    public FaceAdapter(Context context, List<FaceListResult.FaceBean> list) {
        this.context = context;
        this.mDataList = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_face, viewGroup, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final VH vh, final int i) {
        if (mDataList.size() > 0) {
//                vh.icon.setImageDrawable(mDataList.get(i).getDrawable());
            FaceListResult.FaceBean bean = mDataList.get(i);
            vh.name.setText("face" + i +"\t\t\t" + bean.getCtime());
        }

        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
                if (listener != null) {
                    listener.onDeleteClick(i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
//            return 60;
        return mDataList.size();
    }

    OnDeleteListener listener;

    public void setListener(OnDeleteListener listener) {
        this.listener = listener;
    }

    public interface OnDeleteListener {
        void onDeleteClick(int index);
    }
}
