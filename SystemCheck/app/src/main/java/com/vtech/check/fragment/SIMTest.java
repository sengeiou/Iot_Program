package com.vtech.check.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vtech.check.R;
import com.vtech.check.activity.HotspotActivity;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.TELEPHONY_SERVICE;

public class SIMTest extends BaseTest{
    TextView sim_text;
    View rootview;
    private static final int REQUEST_CODE_CONTACT = 101;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            rootview=inflater.inflate(R.layout.sim,container,false);
        sim_text=rootview.findViewById(R.id.sim_text);
        checkPermission();
        return rootview;
    }



    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }


    private boolean checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (permissionsList.size() > 0) {

            ActivityCompat.requestPermissions(getActivity(), permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_CONTACT);
            return false;
        }else {
            setSIMText(readSIMCard());
        }

        return true;

    }


    public void setSIMText(String string){
        sim_text.setText(string);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CONTACT:
                if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setSIMText(readSIMCard());
                    //list is still empty
                }
                else {
                    // Permission Denied
                    Toast.makeText(getActivity(), "用户权限拒绝", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }






    @Override
    public String getTestName() {
        return getContext().getString(R.string.sim_test);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("sim",true);
    }



    @SuppressLint("MissingPermission")
    public String readSIMCard() {
        TelephonyManager tm = (TelephonyManager)getActivity().getSystemService(TELEPHONY_SERVICE);//取得相关系统服务
        StringBuffer sb = new StringBuffer();
        sb.append("SIM状态：");
        switch(tm.getSimState()){ //getSimState()取得sim的状态  有下面6中状态
            case TelephonyManager.SIM_STATE_ABSENT :sb.append("无卡\n");break;
            case TelephonyManager.SIM_STATE_UNKNOWN :sb.append("未知状态\n");break;
            case TelephonyManager.SIM_STATE_NETWORK_LOCKED :sb.append("需要NetworkPIN解锁\n");break;
            case TelephonyManager.SIM_STATE_PIN_REQUIRED :sb.append("需要PIN解锁\n");break;
            case TelephonyManager.SIM_STATE_PUK_REQUIRED :sb.append("需要PUK解锁\n");break;
            case TelephonyManager.SIM_STATE_READY :sb.append("良好\n");break;
        }

        sb.append("SIM卡号：");
        if(tm.getSimSerialNumber()!=null){
            sb.append("" + tm.getSimSerialNumber().toString()+"\n");
        }else{
            sb.append("无法取得SIM卡号\n");
        }
        sb.append("SIM供货商代码：");
        if(tm.getSimOperator().equals("")){
            sb.append("无法取得供货商代码\n");
        }else{
            sb.append("" + tm.getSimOperator().toString()+"\n");
        }
        sb.append("SIM供货商：");
        if(tm.getSimOperatorName().equals("")){
            sb.append("无法取得供货商\n");
        }else{
            sb.append("" + tm.getSimOperatorName().toString()+"\n");
        }
        sb.append("SIM国籍：");
        if(tm.getSimCountryIso().equals("")){
            sb.append("无法取得国籍\n");
        }else{
            sb.append("" + tm.getSimCountryIso().toString()+"\n");
        }
        sb.append("SIM网络运营商：");
        if (tm.getNetworkOperator().equals("")) {
            sb.append("无法取得网络运营商\n");
        } else {
            sb.append("" + tm.getNetworkOperator()+"\n");
        }
        sb.append("SIM网络运营商名称：");
        if (tm.getNetworkOperatorName().equals("")) {
            sb.append("无法取得网络运营商名称\n");
        } else {
            sb.append("" + tm.getNetworkOperatorName()+"\n");
        }
        sb.append("SIM网络类型：");
        if (tm.getNetworkType() == 0) {
            sb.append("无法取得网络类型\n");
        } else {
            sb.append("" + tm.getNetworkType()+"\n");
        }
        return sb.toString();
    }

}
