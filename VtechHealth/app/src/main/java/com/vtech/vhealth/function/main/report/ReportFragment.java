package com.vtech.vhealth.function.main.report;

import android.os.Bundle;

import com.vtech.vhealth.function.base.BaseFragment;

/****
 * @author jason
 *健康页面
 * */
public class ReportFragment extends BaseFragment<ReportDelegate> {
    @Override
    protected Class<ReportDelegate> getDelegateClass() {
        return ReportDelegate.class;
    }

    public static ReportFragment newInstance() {
        ReportFragment fragment = new ReportFragment();
        Bundle args = new Bundle();
        //        args.putInt(AnyKey.I_CLIENT_LINK_KEY, pos);
        fragment.setArguments(args);
        return fragment;
    }


}
