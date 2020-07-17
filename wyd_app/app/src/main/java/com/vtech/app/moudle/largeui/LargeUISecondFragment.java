package com.vtech.app.moudle.largeui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.vtech.app.R;
import com.vtech.app.largeuidb.Constants;
import com.vtech.app.largeuidb.dbutils.ItemUtil;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;
import com.vtech.app.moudle.BaseFragment;
import com.vtech.app.moudle.adapter.ApplicationAdapterLarge;
import com.vtech.app.moudle.largeui.sos.CallingStateListenerService;
import com.vtech.app.moudle.largeui.sos.SOSActivity;
import com.vtech.app.moudle.largeui.sos.SOSBean;
import com.vtech.app.moudle.largeui.sos.ShareUtil;
import com.vtech.app.util.Logger;
import java.util.ArrayList;
import java.util.List;

public class LargeUISecondFragment extends BaseFragment {
    RecyclerView mRecyclerView;

    ApplicationAdapterLarge mAdapter;

    private int mCurrPage;

    public void setmCurrPage(int mCurrPage) {
        this.mCurrPage = mCurrPage;
    }

    private List<LargeUIItem> largeUIItemList = new ArrayList<>();

    private ItemUtil itemUtil;

    public static LargeUISecondFragment newInstance(int currPage) {
        Bundle args = new Bundle();
        args.putInt("CURR_PAGE_INDEX", currPage);
        LargeUISecondFragment fragment = new LargeUISecondFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_second_large_ui;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        mRecyclerView = view.findViewById(R.id.recycle_view);
        GridLayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(getActivity(), Constants.PAGE_COLUMN, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
    }

    @Override
    protected void initData() {
        registerReceiver();
        mCurrPage = getArguments().getInt("CURR_PAGE_INDEX");
        Log.d("LargeUISecondFragment", "initData, page: " + mCurrPage);
        if (largeUIItemList == null) {
            largeUIItemList = new ArrayList<>();
        }
        itemUtil = new ItemUtil(getContext());
        for (LargeUIItem largeUIItem : itemUtil.getListByScreenNum(mCurrPage)) {
            if(largeUIItem != null) {
                if(TextUtils.isEmpty(largeUIItem.getPackageName())) {
                    largeUIItemList.add(largeUIItem);
                }
            }
        }
        mAdapter = new ApplicationAdapterLarge(getActivity(), mCurrPage, itemUtil, largeUIItemList);
        mRecyclerView.setAdapter(mAdapter);
        RecyclerViewUtil util = new RecyclerViewUtil(getActivity(), mRecyclerView);
        util.setOnItemLongClickListener(new RecyclerViewUtil.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position, View view) {
                TextView tv = view.findViewById(R.id.name);
                if (tv.getText().toString().equals("SOS") || tv.getText().toString().equals("紧急求救") || tv.getText().toString().equals("緊急求救")) {
                    List<SOSBean> cList = ShareUtil.getInstance().getList(getActivity().getApplicationContext(), "SOS_CONTACTS_LIST");
                    if (cList != null) {
                        if (cList.size() > 0) {
                            Intent serviceIntent = new Intent(getActivity(), CallingStateListenerService.class);
                            getActivity().startService(serviceIntent);
                        } else {
                            Intent intent = new Intent(getActivity(), SOSActivity.class);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent(getActivity(), SOSActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    @Override
    protected void onLazyLoad() {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getContext().unregisterReceiver(largeUIItemReceiver);
    }

    void registerReceiver() {
        largeUIItemReceiver = new LargeUIItemReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constants.APP_ITEM_COUNT_CHANGED_ACTION);
        getContext().registerReceiver(largeUIItemReceiver, intentFilter);
    }

    LargeUIItemReceiver largeUIItemReceiver;


    private class LargeUIItemReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Logger.d(TAG, "LargeUIItemReceiver action : " + intent.getAction());
            if (intent.getAction().equals(Constants.APP_ITEM_COUNT_CHANGED_ACTION)) {
                Log.d(TAG, "onReceive: itemUtil.getListByScreenNum(mCurrPage)  size "+itemUtil.getListByScreenNum(mCurrPage).size());
                //mAdapter.updateList(itemUtil.getListByScreenNum(mCurrPage));
                mAdapter.notifyDataSetChanged();
            }
        }
    }
}