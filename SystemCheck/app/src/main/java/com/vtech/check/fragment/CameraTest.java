package com.vtech.check.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.vtech.check.R;
import com.vtech.check.utils.PhotoUtils;

import java.io.File;

public class CameraTest extends BaseTest {

    private static final String IMG_NAME = "test.jpg";
    private static final String EXTRAS_CAMERA_FACING = "android.intent.extras.CAMERA_FACING";
    private static final int REQUEST_CAPTURE_IMAGE = 1;

    private int mCameraId = 0;
    View rootView;
    TextView take_text;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera, container, false);
        initView();
        return rootView;
    }

    private void initView() {
        take_text=rootView.findViewById(R.id.take_text);
        take_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoObtainCameraPermission();
            }
        });

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setButtonVisibility(true);
    }

    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                Toast.makeText(getContext(),"您已经拒绝过一次",Toast.LENGTH_SHORT).show();
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                //通过FileProvider创建一个content类型的Uri
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
/*                    File imagePath = new File(getContext().getFilesDir(), "images");
                    File newFile = new File(imagePath, "test.jpg");*/
                    imageUri = FileProvider.getUriForFile(getContext(), "com.vtech.check.fileprovider",fileUri );
                }
                PhotoUtils.takePicture(getActivity(), imageUri, CODE_CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(),"设备没有SD卡！",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            //调用系统相机申请拍照权限回调
            case CAMERA_PERMISSIONS_REQUEST_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(getContext(), "com.vtech.check.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(getActivity(), imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        Toast.makeText(getContext(),"设备没有SD卡！",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getContext(),"请允许打开相机！！",Toast.LENGTH_SHORT).show();
                }
                break;

            }

            default:
        }
    }


    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        setButtonVisibility(true);
 /*       if (resultCode == Activity.RESULT_OK
                && mCameraId < Camera.getNumberOfCameras() - 1) {
        *//*    mCameraId++;
            captureImage();*//*
        } else {
            setButtonVisibility(true);
        }*/
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Delete image file in storage
/*        File file = new File(Environment.getExternalStorageDirectory(), IMG_NAME);
        file.delete();*/
    }

 /*   @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        mCameraId = 0;
        captureImage();
        return true;
    }*/

    @Override
    public String getTestName() {
        return getContext().getString(R.string.camera_title);
    }

    @Override
    public boolean isNeedTest() {
        PackageManager pm = getContext().getPackageManager();
        boolean hasCamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
                || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);
        return hasCamera && getSystemProperties("camera", true);
    }

    private void captureImage() {
        File file = new File(Environment.getExternalStorageDirectory(), IMG_NAME);
        Uri uri = Uri.fromFile(file);

        // Wrap capture intent
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(EXTRAS_CAMERA_FACING, mCameraId);

        startActivityForResult(intent, REQUEST_CAPTURE_IMAGE);
    }
}
