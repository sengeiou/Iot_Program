/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.fl;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.baidu.aip.fl.exception.FaceError;
import com.baidu.aip.fl.model.FaceAdapter;
import com.baidu.aip.fl.model.FaceListResult;
import com.baidu.aip.fl.model.ResponseResult;
import com.baidu.aip.fl.utils.OnResultListener;
import com.baidu.aip.fl.utils.PreferenceUtils;
import com.baidu.aip.fl.utils.Util;
import com.baidu.aip.fl.widget.SwipeLayoutManager;
import com.vtech.face.R;

import java.util.ArrayList;
import java.util.List;

public class FaceLibraryActivity extends AppCompatActivity {
//    RecyclerView mRecyclerView;
//    FaceAdapter mFaceAdapter;

    ListView mListView;
    MyFaceAdapter mMyFaceAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitvity_face_library);


        /*mRecyclerView = findViewById(R.id.recycle_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        manager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(manager);
        mFaceAdapter = new FaceAdapter(this);
        mRecyclerView.setAdapter(mFaceAdapter);
        mFaceAdapter.setListener(new FaceAdapter.OnDeleteListener() {
            @Override
            public void onDeleteClick(final int index) {
                APIService.getInstance().deleteFace(new OnResultListener<ResponseResult>() {
                    @Override
                    public void onResult(ResponseResult result) {
//                        getFaceList();
                        mFaceAdapter.notifyItemRemoved(index);
                    }

                    @Override
                    public void onError(FaceError error) {

                    }
                }, Util.getDeviceIMEI(FaceLibraryActivity.this), mFaceAdapter.getDataList().get(index).getFace_token());
            }
        });*/

        mListView = findViewById(R.id.list_view);
        mMyFaceAdapter = new MyFaceAdapter(this);
        mListView.setAdapter(mMyFaceAdapter);

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    //如果垂直滑动，则需要关闭已经打开的layout
                    SwipeLayoutManager.getInstance().closeCurrentLayout();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mMyFaceAdapter.setListener(new MyFaceAdapter.OnDeleteListener() {
            @Override
            public void onDeleteClick(final int index) {
                APIService.getInstance().deleteFace(new OnResultListener<ResponseResult>() {
                    @Override
                    public void onResult(ResponseResult result) {
                        getFaceList();
                    }

                    @Override
                    public void onError(FaceError error) {
                        getFaceList();
                    }
                }, Util.getDeviceId(FaceLibraryActivity.this), mMyFaceAdapter.getDataList().get(index).getFace_token());
            }
        });


        getFaceList();
    }

    void getFaceList() {
        APIService.getInstance().getFaceList(new OnResultListener<FaceListResult>() {
            @Override
            public void onResult(FaceListResult result) {
//                mFaceAdapter.setDataList(result.getFace_list());
                mMyFaceAdapter.setDataList(result.getFace_list());

                if(result!=null && result.getFace_list()!=null && result.getFace_list().size()>0){
                    PreferenceUtils.setPrefBoolean(getApplication(),FaceProvider.IS_REGIST,true);
                }else {
                    PreferenceUtils.setPrefBoolean(getApplication(),FaceProvider.IS_REGIST,false);
                }
            }

            @Override
            public void onError(FaceError error) {

            }
        }, Util.getDeviceId(this));
    }


    final static class MyFaceAdapter extends BaseAdapter {

        Context context;

        List<FaceListResult.FaceBean> mDataList = new ArrayList<>();

        public MyFaceAdapter(Context context) {
            this.context = context;
        }

        public List<FaceListResult.FaceBean> getDataList() {
            return mDataList;
        }

        public void setDataList(List<FaceListResult.FaceBean> mDataList) {
            this.mDataList = mDataList;
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDataList.size();
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
        public View getView(final int i, View view, ViewGroup viewGroup) {
            ViewHolder holder;
            if (view == null || view.getTag() == null) {
                view = LayoutInflater.from(context).inflate(R.layout.item_face, null);
                holder = new ViewHolder();
                holder.name = view.findViewById(R.id.name);
                holder.delete = view.findViewById(R.id.tv_delete);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }


            if (mDataList.size() > 0) {
//                vh.icon.setImageDrawable(mDataList.get(i).getDrawable());
                FaceListResult.FaceBean bean = mDataList.get(i);
                holder.name.setText("face" + i +"\t\t\t" + bean.getCtime());
            }

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Toast.makeText(context, "click", Toast.LENGTH_LONG).show();
                    if (listener != null) {
                        listener.onDeleteClick(i);
                    }
                }
            });

            return view;
        }

        final static class ViewHolder {
            TextView name;
            View delete;
        }


        OnDeleteListener listener;

        public void setListener(OnDeleteListener listener) {
            this.listener = listener;
        }

        public interface OnDeleteListener {
            void onDeleteClick(int index);
        }
    }
}
