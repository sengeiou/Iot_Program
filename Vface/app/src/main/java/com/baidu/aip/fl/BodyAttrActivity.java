package com.baidu.aip.fl;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.fl.exception.FaceError;
import com.baidu.aip.fl.model.BodyAttrBean;
import com.baidu.aip.fl.utils.OnResultListener;
import com.google.gson.Gson;
import com.vtech.face.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BodyAttrActivity extends BaseActivity {
    public static final String TAG = "BodyAttrActivity";
    // 相册选择回传吗
    public static final int PHOTO_REQUEST_GALLERY = 102;

    TextView resultTv;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_body_attr);
        resultTv = findViewById(R.id.result_tv);
        findViewById(R.id.choose_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAlbum();
            }
        });
    }

    private String mPhotoPath, mOriginPath;
    private File mPhotoFile;

    int imgWidth, imgHeight;


    int TOP = 1;
    int BOTTOM = 2;
    int LEFT = 6;
    int RIGHT = 3;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i(TAG, "onActivityResult requestCode：" + requestCode + "，resultCode：" + resultCode);

        if (requestCode == PHOTO_REQUEST_GALLERY && resultCode == RESULT_OK) {
            //调用相册
            Cursor cursor = getContentResolver().query(data.getData(), new String[]{MediaStore.Images.Media.DATA}, null, null, null);
            //游标移到第一位，即从第一位开始读取
            if (cursor != null) {
                cursor.moveToFirst();
                mPhotoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                cursor.close();


                //获取Options对象
                BitmapFactory.Options options = new BitmapFactory.Options();
                //仅做解码处理，不加载到内存
                options.inJustDecodeBounds = true;
                //解析文件
                BitmapFactory.decodeFile(mPhotoPath, options);
                //获取宽高
                imgWidth = options.outWidth;
                imgHeight = options.outHeight;
                Log.i(TAG, "imgWidth : " + imgWidth + " imgHeight : " + imgHeight);
                if (imgHeight > 4000 || imgHeight > 1200) {
                    compress(mPhotoPath, imgHeight / 600);
                    mPhotoFile = new File(mOriginPath);
                    BitmapFactory.decodeFile(mOriginPath, options);
                    //获取宽高
                    imgWidth = options.outWidth;
                    imgHeight = options.outHeight;
                    Log.i(TAG, "压缩后 imgWidth : " + imgWidth + " imgHeight : " + imgHeight);
                } else {
                    mPhotoFile = new File(mPhotoPath);
                }
                /*APIService.getInstance().bodyAttr(new OnResultListener() {
                    @Override
                    public void onResult(Object result) {
                        final String resp = (String) result;
                        try {
                            JSONObject jsonObject = new JSONObject(resp);
                            if (jsonObject.has("error_code")) {

                            } else {
                                BodyAttrBean bean = new Gson().fromJson(resp, BodyAttrBean.class);
                                int top = bean.getPerson_info().get(0).getLocation().getTop();
                                int left = bean.getPerson_info().get(0).getLocation().getLeft();

                                int wd = bean.getPerson_info().get(0).getLocation().getWidth();
                                int ht = bean.getPerson_info().get(0).getLocation().getHeight();

                                int derection = 0;

                                int leftResult = imgWidth / left;

                                if (leftResult > 5) {
                                    derection = RIGHT;
                                } else if (leftResult < 2) {
                                    derection = LEFT;
                                } else {
                                    derection = 0;
                                }

                                int topResult = imgHeight / top;
                                if (topResult > 5) {
                                    derection = BOTTOM;
                                } else if (topResult < 2) {
                                    derection = TOP;
                                } else {
                                    derection = 0;
                                }

                                Log.i(TAG, "bodyAttr result derection : " + derection);

                                final int fDeretion = derection;

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        hideProgressDilog();
                                        resultTv.setText(resp + " \n\t" + "result derection : " + fDeretion);
                                        Toast.makeText(BodyAttrActivity.this, resp, Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(FaceError error) {

                    }
                }, mPhotoFile);*/
            }
        }
    }


    /**
     * @param inSampleSize 可以根据需求计算出合理的inSampleSize
     */
    public void compress(String path, int inSampleSize) {
        mOriginPath = Environment.getExternalStorageDirectory() + File.separator + "resultImg.jpg";
        File originFile = new File(path);
        BitmapFactory.Options options = new BitmapFactory.Options();
        //设置此参数是仅仅读取图片的宽高到options中，不会将整张图片读到内存中，防止oom
        options.inJustDecodeBounds = true;
        Bitmap emptyBitmap = BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);

        options.inJustDecodeBounds = false;
        options.inSampleSize = inSampleSize;
        Bitmap resultBitmap = BitmapFactory.decodeFile(originFile.getAbsolutePath(), options);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);

        try {
            FileOutputStream fos = new FileOutputStream(mOriginPath);
            fos.write(bos.toByteArray());
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 调用手机相册
     */
    private void openAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }
}
