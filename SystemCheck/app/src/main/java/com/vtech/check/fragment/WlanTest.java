package com.vtech.check.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vtech.check.R;
import com.vtech.check.activity.WifiActivity;
import com.vtech.check.utils.WifiUtil;

import java.util.ArrayList;
import java.util.List;

public class WlanTest extends BaseTest implements View.OnClickListener, AdapterView.OnItemClickListener {
    View rootView;
    private Button search_btn;
    private ListView wifi_lv;
    private WifiUtil mUtils;
    private List<String> result;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        rootView=inflater.inflate(R.layout.wlan,container,false);
        initView();
        setLiteners();
        return rootView;

    }

    private void initView() {
        mUtils = new WifiUtil(getContext());

        this.search_btn = (Button) rootView.findViewById(R.id.search_btn);
        this.wifi_lv = (ListView) rootView.findViewById(R.id.wifi_lv);
    }

    private void setLiteners() {

        search_btn.setOnClickListener(this);
        wifi_lv.setOnItemClickListener(this);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }
    @Override
    public String getTestName() {
        return getContext().getString(R.string.wlan_test);
    }

    @Override
    public boolean isNeedTest() {
        return getSystemProperties("wlan", true);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn) {
            checkPermission();
        }
    }

    private void checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionsList.size() > 0) {

            ActivityCompat.requestPermissions((getActivity()), permissionsList.toArray(new String[permissionsList.size()]),
                    1);
        }else {
            new MyAsyncTask().execute();
        }

    }


    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            //扫描附近WIFI信息
            result = mUtils.getScanWifiResult();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            progressDismiss();
            initListViewData();
        }
    }

    @SuppressLint("ResourceType")
    private void initListViewData() {
        if (null != result && result.size() > 0) {
            wifi_lv.setAdapter(new ArrayAdapter<String>(
                    getActivity().getApplicationContext(), R.layout.wifi_list_item,
                    R.id.ssid, result));
        } else {
            wifi_lv.setEmptyView(rootView.findViewById(R.layout.list_empty));
        }
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView tv = (TextView) view.findViewById(R.id.ssid);
        if (!TextUtils.isEmpty(tv.getText().toString())) {
            Intent in = new Intent(getActivity(), WifiActivity.class);
            in.putExtra("ssid", tv.getText().toString());
            startActivity(in);
        }
    }
}
