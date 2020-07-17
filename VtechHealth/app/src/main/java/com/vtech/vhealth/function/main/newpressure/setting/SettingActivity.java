package com.vtech.vhealth.function.main.newpressure.setting;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.vtech.vhealth.R;
import com.vtech.vhealth.api.UserAPI;
import com.vtech.vhealth.api.okhttp.BodyCallback;
import com.vtech.vhealth.function.base.BaseActivity;
import com.vtech.vhealth.function.bean.IconBean;
import com.vtech.vhealth.function.bean.ServerUser;
import com.vtech.vhealth.function.bean.UserBean;
import com.vtech.vhealth.function.main.newpressure.PermissionDialog;
import com.vtech.vhealth.function.utils.JsonUtil;
import com.vtech.vhealth.function.utils.SpUtils;
import com.vtech.vhealth.function.utils.ToastUtil;
import com.vtech.vhealth.function.utils.UriUtils;
import com.vtech.vhealth.function.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.Call;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;

///用户信息设置界面
public class SettingActivity extends BaseActivity<SettingDelegate> {
    @Override
    protected Class getDelegateClass() {
        return SettingDelegate.class;
    }

    private static final String TAG = "SettingActivity";
    public static final String USER_KEY = "user_key";
    public static final String USER_AUTHORITY = "com.vtech.vhealth.healthprovider.fileprovider";

    private int curUser;
    private Uri imageUri;
    public static final int TAKE_PHOTO=11;
    private  UserBean user ;
    private String deviceID;

    public static void start(Context context,int user) {
        Intent intent = new Intent(context, SettingActivity.class);
        intent.putExtra(TAG,user);
        context.startActivity(intent);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        deviceID= Utils.getDeviceId(this);

        MySpinnerAdapter  adapter = new MySpinnerAdapter(this,getResources().getStringArray(R.array.str_sex));
        viewDelegate.getSex().setAdapter(adapter);

        viewDelegate.addClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.tv_ok:
                         user =  viewDelegate.getUserBean();
                        if (imageUri != null) {
                            user.setIcon(imageUri.getPath());
                        }
                        if (TextUtils.isEmpty(deviceID)) {
                            deviceID= Utils.getDeviceId(SettingActivity.this);
                        }
                        UserAPI.updateUserInfo(deviceID,USER_KEY+curUser,user,new BodyCallback<String>(){

                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.showShort(R.string.str_userinfo_fail);
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                if (isSuccess()) {
                                    ToastUtil.showShort(R.string.str_userinfo_success);
                                    SpUtils.set(USER_KEY+curUser,JsonUtil.beanToJson(user));
                                    finish();
                                }else {
                                    ToastUtil.showShort(getMsg());
                                }

                            }
                        });


                        break;

                    case R.id.tv_cancle:
                        finish();
                        break;
                    case R.id.iv_icon:
                        checkDefaultPermission();
                        break;
                }
            }
        });
        getUser();
        UserAPI.getUserInfo(deviceID, USER_KEY + curUser, new BodyCallback<ServerUser>() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(ServerUser user, int id) {
                Log.i(TAG," response="+user);
                if (isSuccess()) {
                    UserBean userBean=new UserBean();
                    userBean.setIcon(user.getPortrait());
                    userBean.setUid(user.getIdentity());
                    userBean.setSex(""+user.getSex());
                    userBean.setArea(user.getAddress());
                    userBean.setAge(user.getAge());
                    userBean.setUserId(user.getDevice_user_id());
                    userBean.setUserName(user.getName());
                    viewDelegate.initUser(userBean,curUser);
                    SpUtils.set(USER_KEY+curUser,JsonUtil.beanToJson(userBean));
                }
            }
        });
    }

    private void checkDefaultPermission() {
        if ( ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 2);
        }else {
            gotoImg();
        }
    }

    private void gotoImg() {
//        File outputImage=new File(getExternalCacheDir(),curUser+"outputImage.jpg");
//        try {
//            if (outputImage.exists()){
//                outputImage.delete();
//            }
//            outputImage.createNewFile();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        imageUri = UriUtils.getUri(this,outputImage);
        //启动相机程序
        Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION );
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION );
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent,TAKE_PHOTO);
    }


    private void getUser(){
        Intent intent=getIntent();
      if (intent != null) {
          curUser =intent.getIntExtra(TAG,1);
          String userStr= SpUtils.get(USER_KEY+curUser,"");
          if (!TextUtils.isEmpty(userStr)) {
              UserBean user= JsonUtil.jsonToBean(userStr,UserBean.class);
              viewDelegate.initUser(user,curUser);
          }
      }
  }

  private void initIcon(int user){
      String userStr = SpUtils.get(SettingActivity.USER_KEY + user, "");
      if (!TextUtils.isEmpty(userStr)) {
          try {

              File path=new File(getExternalCacheDir(),user+"outputImage.jpg");
              Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(UriUtils.getUri(this, path)));
              viewDelegate.getIcon().setImageBitmap(bitmap);
          } catch (FileNotFoundException e) {
              e.printStackTrace();
          }
      }
  }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    try {
                        //将拍摄的照片显示出来
//                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
//                        user =  viewDelegate.getUserBean();
//                        SpUtils.set(USER_KEY+curUser,JsonUtil.beanToJson(user));
                        if (data != null) {
                            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                            viewDelegate.getIcon().setImageBitmap(bitmap);
                            saveBitmap(bitmap);
                        }

                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {//选择了“始终允许”
                    gotoImg();
                } else {
                    final PermissionDialog dialog =new PermissionDialog(this);
                    dialog.setClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                             switch (view.getId()){
                                 case R.id.tv_share:
                                     break;
                                 case R.id.tv_sure:
                                     getAppDetailSettingIntent(SettingActivity.this);
                                     break;
                             }
                        }
                    });
                    dialog.showDialog();

                }
            }
        }
    }

    public void saveBitmap(Bitmap bitmap) {
        File f=new File(getExternalCacheDir(),curUser+"outputImage.jpg");
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            if (TextUtils.isEmpty(deviceID)) {
                deviceID= Utils.getDeviceId(this);
            }
            UserAPI.updateUserIcon(deviceID, USER_KEY + curUser, f, new BodyCallback<IconBean>() {
                @Override
                public void onError(Call call, Exception e, int id) {

                    ToastUtil.showShort(R.string.str_load_img_fail);
                }

                @Override
                public void onResponse(IconBean response, int id) {
                    if (isSuccess()) {
                         Log.e(TAG," response="+response.getImg());
                        ToastUtil.showShort(R.string.str_load_img_success );
                        String userStr= SpUtils.get(USER_KEY+curUser,"");
                        if (!TextUtils.isEmpty(userStr)) {
                            UserBean userBean= JsonUtil.jsonToBean(userStr,UserBean.class);
                            userBean.setIcon(response.getImg());
                            SpUtils.set(USER_KEY+curUser,JsonUtil.beanToJson(userBean));
                            viewDelegate.initUser(userBean,curUser);
                        }
                    }else {
                        ToastUtil.showShort(getMsg());
                    }
                }
            });
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "已经保存出错FileNotFoundException "+e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            Log.i(TAG, "已经保存出错IOException "+e.getMessage());
        }
    }

    private void getAppDetailSettingIntent(Context context){
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if(Build.VERSION.SDK_INT >= 9){
            intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            intent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if(Build.VERSION.SDK_INT <= 8){
            intent.setAction(Intent.ACTION_VIEW);
            intent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
            intent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(intent);
    }



}



