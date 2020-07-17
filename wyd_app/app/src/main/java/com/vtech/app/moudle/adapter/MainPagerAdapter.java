package com.vtech.app.moudle.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.vtech.app.moudle.home.HomeFragment;
import com.vtech.app.moudle.main.MainFragment;
import com.vtech.app.moudle.report.ReportFragment;

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] mFragments;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public MainPagerAdapter(FragmentManager fm, Fragment[] fragments) {
        super(fm);
        this.mFragments = fragments;
    }

    public Fragment[] getmFragments() {
        return mFragments;
    }

    public void setmFragments(Fragment[] mFragments) {
        this.mFragments = mFragments;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if(mFragments[0] == null){
                    mFragments[0] = ReportFragment.newInstance();
                }
                return mFragments[0];
            case 1:
                if(mFragments[1] == null){
                    mFragments[1] = MainFragment.newInstance();
                }
                return mFragments[1];
            case 2:
                if(mFragments[2] == null){
                    mFragments[2] = HomeFragment.newInstance();
                }
                return mFragments[2];
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mFragments.length;
    }
}
