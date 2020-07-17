package com.vtech.app.moudle.adapter;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainPagerAdapterLargeUI extends FragmentPagerAdapter {

    private List<Fragment> mFragments = new ArrayList<>();
    private FragmentManager manager;

    public MainPagerAdapterLargeUI(FragmentManager fm, List<Fragment> fragments) {
        super(fm);
        this.manager = fm;
        this.mFragments = fragments;
    }

    public void updateFragmentList(List<Fragment> mFragments) {
        for (Fragment fragment : this.mFragments) {
            manager.beginTransaction().remove(fragment);
        }

        this.mFragments = mFragments;
        notifyDataSetChanged();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        //return super.getItemPosition(object);
        return PagerAdapter.POSITION_NONE;
    }
}