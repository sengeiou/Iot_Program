package com.vtech.app.moudle.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vtech.app.R;

import java.util.List;

public class NormalAdapter extends RecyclerView.Adapter<NormalAdapter.VH>{
    //② 创建ViewHolder
    public static class VH extends RecyclerView.ViewHolder{
        TextView name;
        public VH(View v) {
            super(v);
            name = (TextView) v.findViewById(R.id.name);
        }
    }

    private List mDatas;

    public NormalAdapter() {
    }

    //③ 在Adapter中实现3个方法
    @Override
    public void onBindViewHolder(VH holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 5;
//        return mDatas.size();
    }


    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        //LayoutInflater.from指定写法
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cost, parent, false);
        return new VH(v);
    }
}