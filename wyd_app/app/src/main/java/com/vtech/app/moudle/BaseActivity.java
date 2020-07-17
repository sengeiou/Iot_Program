package com.vtech.app.moudle;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.vtech.app.R;
import com.vtech.app.util.Logger;
import com.vtech.voiceassistant.IVoiceAssistant;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {
    public String TAG = "";

    private IVoiceAssistant myBinder;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            myBinder = IVoiceAssistant.Stub.asInterface(iBinder);
//            createAndShowToast();
            /*
            try {
                Logger.i(TAG, "onServiceConnected");
                myBinder.play("小易小易，探索AI语音");

            } catch (RemoteException E) {

            }*/
            Log.i(TAG,"IVoiceAssistant onServiceConnected");
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Logger.i(TAG, "onServiceDisconnected");
        }
    };

    public void createAndShowToast() {
        LayoutInflater inflate = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View toastRoot = inflate.inflate(R.layout.toast_voice_notify, null);
        //设置自定义宽高
        ConstraintLayout layout = toastRoot.findViewById(R.id.linear_inside);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;
        layout.setLayoutParams(new FrameLayout.LayoutParams(screenWidth, screenHeight / 10));
        //声明Toast
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.CENTER, 0, screenHeight / 4 * 10 / 9);
        //给Toast设置布局
        toast.setView(toastRoot);
        //设置弹出时长
        toast.setDuration(Toast.LENGTH_LONG);
        //弹出Toast
        toast.show();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        Logger.d(TAG, "onCreate");
        setContentView(getLayoutID());
//      ButterKnife.bind(this);
        initView();
        initData();

        Intent intent = new Intent();
        intent.setClassName("com.vtech.voiceassistant", "com.vtech.voiceassistant.VoiceAssistantService");
        //startService(intent);
        bindService(intent, conn, BIND_AUTO_CREATE);
        requestPermissions(99);
    }

    // 请求权限
    public void requestPermissions(int requestCode) {
        try {
            if (Build.VERSION.SDK_INT >= 23) {
                ArrayList<String> requestPerssionArr = new ArrayList<>();
                int hasCamrea = checkSelfPermission(Manifest.permission.CAMERA);
                if (hasCamrea != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.CAMERA);
                }

                int hasSdcardRead = checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
                if (hasSdcardRead != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.READ_EXTERNAL_STORAGE);
                }

                int hasSdcardWrite = checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (hasSdcardWrite != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }

                int hasPhoneState = checkSelfPermission(Manifest.permission.READ_PHONE_STATE);
                if (hasPhoneState != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.READ_PHONE_STATE);
                }

                int hasContactState = checkSelfPermission(Manifest.permission.READ_CONTACTS);
                if (hasContactState != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.READ_CONTACTS);
                }

                int hasWriteContactState = checkSelfPermission(Manifest.permission.WRITE_CONTACTS);
                if (hasWriteContactState != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.WRITE_CONTACTS);
                }

                int hasReadCallLog = checkSelfPermission(Manifest.permission.READ_CALL_LOG);
                if (hasReadCallLog != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.READ_CALL_LOG);
                }

                int hasWriteCallLog = checkSelfPermission(Manifest.permission.WRITE_CALL_LOG);
                if (hasWriteCallLog != PackageManager.PERMISSION_GRANTED) {
                    requestPerssionArr.add(Manifest.permission.WRITE_CALL_LOG);
                }

                // 是否应该显示权限请求
                if (requestPerssionArr.size() >= 1) {
                    String[] requestArray = new String[requestPerssionArr.size()];
                    for (int i = 0; i < requestArray.length; i++) {
                        requestArray[i] = requestPerssionArr.get(i);
                    }
                    requestPermissions(requestArray, requestCode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        boolean flag = false;
        for (int i = 0; i < permissions.length; i++) {
            if (PackageManager.PERMISSION_GRANTED == grantResults[i]) {
                flag = true;
            }
        }
    }

    protected abstract int getLayoutID();

    protected abstract void initView();

    protected abstract void initData();

    public IVoiceAssistant getAIDLServer(){
        if(myBinder !=null){
            return myBinder;
        }
        return null;
    }


    @Override
    protected void onStart() {
        super.onStart();
        Logger.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Logger.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.d(TAG, "onDestroy");
//      ButterKnife.unbind(this);
        unbindService(conn);
    }
}
