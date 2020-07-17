package com.vtech.vhealth.function.main;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.vtech.vhealth.R;
import com.vtech.vhealth.constant.AnyKey;
import com.vtech.vhealth.function.base.BaseActivity;
import com.vtech.vhealth.function.base.BaseFragment;
import com.vtech.vhealth.function.main.ecg.EcgFragment;
import com.vtech.vhealth.function.main.newpressure.PressureFragment;
import com.vtech.vhealth.function.main.pressure.BpressureFragment;
import com.vtech.vhealth.function.utils.SpUtils;
import com.vtech.vhealth.function.widget.AutofitHeightViewPager;

import java.util.ArrayList;
import java.util.List;

///健康 首页 包含 血压，心率，健康报告 三个fragment
public class MainHealthActivity extends BaseActivity<MainHealthDelegate> {
    @Override
    protected Class getDelegateClass() {
        return MainHealthDelegate.class;
    }

    private static final String TAG = "MainHealthActivity";
    private FragmentPagerAdapter mAdapter;
    private AutofitHeightViewPager mViewPager;
    private List<BaseFragment> mFragments = new ArrayList<>();
    //选项卡名称
    private String[] mTitle;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainHealthActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        initFragTab();
        initDialog();
    }

    private void initDialog() {
        boolean first = SpUtils.get(AnyKey.KEY_HEALTH_HINT, true);
        if (first) {
            showHintDialog();
        }
    }
    @Override
    public Resources getResources() {
        Resources res = super.getResources();
        Configuration config=new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config,res.getDisplayMetrics() );
        return res;
    }
    /***
     * 初始化fragment选项卡
     * */
    private void initFragTab() {
        TabLayout tabLayout = viewDelegate.getTabLayout();
        tabLayout.setVisibility(View.VISIBLE);
        mViewPager = viewDelegate.getVp();
        mTitle = new String[]{getString(R.string.health_tab_bpressure), getString(R.string.health_tab_ecg), getString(R.string.health_tab_report)};
        mFragments.add(PressureFragment.newInstance());
        mFragments.add(EcgFragment.newInstance());
//        mFragments.add(ReportFragment.newInstance());

        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager(), 1) {
            @Override
            public int getCount() {
                return mFragments.size();
            }

            @Override
            public BaseFragment getItem(int arg0) {
                return mFragments.get(arg0);
            }
        };
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOffscreenPageLimit(mAdapter.getCount());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mViewPager.updateHeight(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tabLayout.setupWithViewPager(mViewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));
            }
        }
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        mViewPager.setCurrentItem(0);

    }


    /*获取头部选项卡布局*/
    private View getTabView(int position) {
        View v = LayoutInflater.from(this).inflate(R.layout.tab_item_title, null);
        TextView tv = v.findViewById(R.id.health_tab_item_tv);
        tv.setText(mTitle[position]);
        return tv;
    }


    private void showHintDialog() {
        HealthAlertDialog hintDialog = new HealthAlertDialog(this, R.style.healthDialog);
        hintDialog.showDialog();
    }

}



