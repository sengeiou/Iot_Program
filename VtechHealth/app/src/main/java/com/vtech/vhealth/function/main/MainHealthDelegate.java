package com.vtech.vhealth.function.main;

import com.google.android.material.tabs.TabLayout;
import com.vtech.vhealth.R;
import com.vtech.vhealth.function.base.AppDelegate;
import com.vtech.vhealth.function.widget.AutofitHeightViewPager;

public class MainHealthDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_health_main;
    }


    public TabLayout getTabLayout() {
        return get(R.id.tab_health);
    }

    public AutofitHeightViewPager getVp() {
        return get(R.id.vp_health);
    }
}
