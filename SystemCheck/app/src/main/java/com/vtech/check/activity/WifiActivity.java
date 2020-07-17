package com.vtech.check.activity;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.vtech.check.R;
import com.vtech.check.utils.WifiUtil;

import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends Activity implements OnClickListener {
    private Button connect_btn;
    private TextView wifi_ssid_tv;
    private EditText wifi_pwd_tv;
    private WifiUtil mUtils;
    // wifi之ssid
    private String ssid;
    private String pwd;
    private ProgressDialog progressdlg = null;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    showToast("WIFI连接成功");
                    finish();
                    break;
                case 1:
                    showToast("WIFI连接失败");
                    break;

            }
            progressDismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect);
        mUtils = new WifiUtil(this);
        findViews();
        setLiteners();
        initDatas();
    }

    /**
     * init dialog
     */
    private void progressDialog() {
        progressdlg = new ProgressDialog(this);
        progressdlg.setCanceledOnTouchOutside(false);
        progressdlg.setCancelable(false);
        progressdlg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressdlg.setMessage(getString(R.string.wait_moment));
        progressdlg.show();
    }

    /**
     * dissmiss dialog
     */
    private void progressDismiss() {
        if (progressdlg != null) {
            progressdlg.dismiss();
        }
    }

    private void initDatas() {
        ssid = getIntent().getStringExtra("ssid");
        if (!TextUtils.isEmpty(ssid)) {
            ssid = ssid.replace("\"", "");
        }
        this.wifi_ssid_tv.setText(ssid);
    }

    private void findViews() {
        this.connect_btn = (Button) findViewById(R.id.connect_btn);
        this.wifi_ssid_tv = (TextView) findViewById(R.id.wifi_ssid_tv);
        this.wifi_pwd_tv = (EditText) findViewById(R.id.wifi_pwd_tv);
    }

    private void setLiteners() {
        connect_btn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.connect_btn) {// 下一步操作
            pwd = wifi_pwd_tv.getText().toString();
            // 判断密码输入情况
            if (TextUtils.isEmpty(pwd)) {
                Toast.makeText(this, "请输入wifi密码", Toast.LENGTH_SHORT).show();
                return;
            }
            progressDialog();
            // 在子线程中处理各种业务
            dealWithConnect(ssid, pwd);
        }
    }

    private void dealWithConnect(final String ssid, final String pwd) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                // 检验密码输入是否正确
                boolean pwdSucess = mUtils.connectWifiTest(ssid, pwd);
                try {
                    Thread.sleep(4000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (pwdSucess) {
                    mHandler.sendEmptyMessage(0);
                } else {
                    mHandler.sendEmptyMessage(1);
                }
            }
        }).start();
    }

    private void showToast(String str) {
        Toast.makeText(WifiActivity.this, str, Toast.LENGTH_SHORT).show();
    }
}
