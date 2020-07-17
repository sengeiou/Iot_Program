package com.vtech.check.fragment;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;

import com.vtech.check.R;

public class TelephoneTest extends BaseTest{

    View rootview;
    Button btn_tel;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            rootview=inflater.inflate(R.layout.telephone,container,false);
        initView();
        setButtonVisibility(true);
        return rootview;
    }

    private void initView() {
        btn_tel=rootview.findViewById(R.id.btn_tel);
        btn_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callTelphone();
            }
        });
    }

    private void callTelphone() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        Uri data = Uri.parse("tel:" + "112");
        intent.setData(data);
        startActivity(intent);

//        Intent intent=new Intent(Intent.ACTION_CALL,Uri.parse("tel:"+"112"));
//        startActivity(intent);

//        ComponentName comp = new ComponentName("com.android.phone",
//                "com.android.phone.EmergencyDialer");
//        Intent intent = new Intent();
//        intent.setComponent(comp);
//        Uri data = Uri.parse("tel:" + "112");
//        intent.setData(data);
//        startActivity(intent);

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    @Override
    public String getTestName() {
        return getContext().getString(R.string.telephone_test);
    }


    @Override
    public boolean isNeedTest() {
        return getSystemProperties("telephone",true);
    }

}
