package com.vtech.check.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vtech.check.MyApplication;
import com.vtech.check.R;
import com.vtech.check.utils.WifiUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HotspotActivity extends Activity implements OnClickListener,
        AdapterView.OnItemClickListener {
    private Button search_btn;
    private ListView wifi_lv;
    private WifiUtil mUtils;
    private List<String> result;
    private ProgressDialog progressdlg = null;
    private static final int REQUEST_CODE_CONTACT = 101;

    String [] permissions = {Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.GET_ACCOUNTS};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspot);
        mUtils = new WifiUtil(this);
        findViews();
        setLiteners();
    }

    private void findViews() {
        this.search_btn = (Button) findViewById(R.id.search_btn);
        this.wifi_lv = (ListView) findViewById(R.id.wifi_lv);
    }

    private void setLiteners() {
        search_btn.setOnClickListener(this);
        wifi_lv.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.search_btn) {
//            showDialog();
            checkPermission();
        /*    new MyAsyncTask().execute();*/
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CONTACT:
                if (permissions.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED ||
                        (permissions.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                                grantResults[1] == PackageManager.PERMISSION_GRANTED)){
                    new MyAsyncTask().execute();
                    //list is still empty
                }
                else {
                    // Permission Denied
                    Toast.makeText(this, "用户权限拒绝", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


    private boolean checkPermission() {

        List<String> permissionsList = new ArrayList<String>();

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        if (permissionsList.size() > 0) {

            ActivityCompat.requestPermissions((Activity) this, permissionsList.toArray(new String[permissionsList.size()]),
                    1);
            return false;
        }else {
            new MyAsyncTask().execute();
        }



//        //申请权限
//        if (Build.VERSION.SDK_INT >= 23) {
//
//            String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION};
//            //验证是否许可权限
//            for (String str : permissions) {
//                if (checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    //申请权限
//                    this.requestPermissions(permissions, REQUEST_CODE_CONTACT);
//                }
//            }
//        }



        return true;
    }




    /**
     * init dialog and show
     */
    private void showDialog() {
        progressdlg = new ProgressDialog(this);
        progressdlg.setCanceledOnTouchOutside(false);
        progressdlg.setCancelable(false);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.wait_moment));
        progressdlg.show();
    }

    /**
     * dismiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
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
                    getApplicationContext(), R.layout.wifi_list_item,
                    R.id.ssid, result));
        } else {
            wifi_lv.setEmptyView(findViewById(R.layout.list_empty));
        }
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        TextView tv = (TextView) arg1.findViewById(R.id.ssid);
        if (!TextUtils.isEmpty(tv.getText().toString())) {
            Intent in = new Intent(HotspotActivity.this, WifiActivity.class);
            in.putExtra("ssid", tv.getText().toString());
            startActivity(in);
        }
    }
}
