package com.vtech.app.moudle.largeui;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.vtech.app.App;
import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.largeuidb.Constants;
import com.vtech.app.largeuidb.dbutils.ItemUtil;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;
import com.vtech.app.moudle.BaseActivity;
import java.util.List;

public class PickAppActivity extends BaseActivity {

    RecyclerView mRecyclerView;
    ImageView backIcon;
    PickAppAdapter mAdapter;
    private Bundle mPosBundle;
    LargeUIContract.Presenter mPresenter;

    @Override
    protected int getLayoutID() {
        return R.layout.activity_pick_app;
    }

    @Override
    protected void initView() {
        backIcon = (ImageView) findViewById(R.id.back_icon);
        backIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               finish();
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PickAppAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setCallback(new PickAppAdapter.ClickCallback() {
            @Override
            public void setResult(AppInfo appInfo) {
                setResultData(appInfo);
            }
        });
    }

    @Override
    protected void initData() {
        if (getIntent() != null) {
            mPosBundle = getIntent().getExtras();
        }
        List<AppInfo> appInfos = App.getInstance().getAppInfoList();
        mAdapter.setDataList(appInfos);
    }

    private void setResultData(AppInfo appInfo) {
        if (mPosBundle != null) {
            LargeUIItem item = new LargeUIItem();
            item.setType(Constants.ITEM_TYPE_NORMAL);
            item.setItemName(appInfo.getAppName());
            item.setIsApp(true);
            item.setIsModify(true);
            item.setIsMove(true);
            item.setPackageName(appInfo.getPackageName());
            item.setScreen(mPosBundle.getInt("PAGE_INDEX"));
            item.setCellX(mPosBundle.getInt("CELLX"));
            item.setCellY(mPosBundle.getInt("CELLY"));
            new ItemUtil(this).insert(item);
        }
        finish();
    }
}
