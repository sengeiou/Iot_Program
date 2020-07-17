package com.vtech.app.moudle.largeui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.vtech.app.App;
import com.vtech.app.BuildConfig;
import com.vtech.app.R;
import com.vtech.app.data.bean.AppInfo;
import com.vtech.app.largeuidb.Constants;
import com.vtech.app.largeuidb.dbutils.ItemUtil;
import com.vtech.app.largeuidb.largeuibean.LargeUIItem;
import com.vtech.app.moudle.adapter.SubItemApplicationAdapter;
import com.vtech.app.moudle.largeui.contacts.SpaceItemDecoration;

import java.util.ArrayList;
import java.util.List;

import static com.vtech.app.largeuidb.Constants.DEFAULT_CHAT_APPS_PACKAGE_NAME;

public class SubItemActivity extends Activity {
    private final String TAG = "SubItemActivity";
    private RecyclerView mRecyclerView;
    private TextView subItemTitle;

    private SubItemApplicationAdapter mAdapter;

    //private int subItemType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_item_largeui);
        initView();
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setDecorFlag();
    }

    /*
        @Override
        protected int getLayoutID() {
            return R.layout.activity_sub_item_largeui;
        }
    */
    //@Override
    protected void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recycle_view);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(10));
        GridLayoutManager mLayoutManager;
        mLayoutManager = new GridLayoutManager(SubItemActivity.this, 2, OrientationHelper.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        subItemTitle = (TextView) findViewById(R.id.item_title);
    }

    //@Override
    protected void initData() {
        Log.d(TAG, "initData");
        //subItemType = getIntent().getIntExtra("ITEM_TYPE", -1);
        //if (subItemType < 0) {
        //    return;
        //}
        ItemUtil itemUtil = new ItemUtil(App.getInstance());
        LargeUIItem itemParent = itemUtil.getItem(Constants.ITEM_TYPE_CHAT);
        Log.d(TAG, "initData 2");
        if (itemParent != null) {
            Log.d(TAG, "initData 3");
            List<LargeUIItem> itemList = itemUtil.getContainerItem(itemParent.getId());

            List<AppInfo> mDataList = new ArrayList<>();

            final PackageManager packageManager = getPackageManager();
            final Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            // get all apps
            final List<ResolveInfo> apps = packageManager.queryIntentActivities(mainIntent, 0);
            for (int i = 0; i < apps.size(); i++) {
                String packageName = apps.get(i).activityInfo.packageName;
                Log.i(TAG, "all getAppProcessName: " + packageName + " , AppName : " + apps.get(i).activityInfo.loadLabel(packageManager).toString());
                //只添加微信 和 whats_app
                if (packageName.contains(DEFAULT_CHAT_APPS_PACKAGE_NAME[0]) || packageName.contains(DEFAULT_CHAT_APPS_PACKAGE_NAME[1])) {
                    AppInfo bean = new AppInfo();
                    bean.setAppName(apps.get(i).activityInfo.loadLabel(packageManager).toString());
                    bean.setPackageName(packageName);
                    mDataList.add(bean);
                }
            }
            if (mDataList != null && mDataList.size() > 0) {
                Log.d(TAG, "initData 4, size: " + mDataList.size());
                mAdapter = new SubItemApplicationAdapter(SubItemActivity.this, mDataList);
                mRecyclerView.setAdapter(mAdapter);
            }
        }
    }

    private void setDecorFlag() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option;
            if (BuildConfig.LARGE_UI_SWITCH) {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            } else {
                option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            }

            decorView.setSystemUiVisibility(option);
            if (!BuildConfig.LARGE_UI_SWITCH) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
            //4.4到5.0
        }
    }
}
