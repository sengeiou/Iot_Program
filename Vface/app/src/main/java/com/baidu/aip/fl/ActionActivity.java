package com.baidu.aip.fl;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.aip.fl.exception.FaceError;
import com.baidu.aip.fl.utils.OnResultListener;
import com.vtech.face.R;

import java.io.File;

public class ActionActivity extends BaseActivity {
    public static final String TAG = "ActionActivity";
    String VIDEOPATH;

    TextView resultTv;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action);

        findView();
        addListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void findView() {
        resultTv = findViewById(R.id.result_tv);
    }

    private void addListener() {
        findViewById(R.id.choose_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choiceVideo();
            }
        });
    }


    /**
     * 从相册中选择视频
     */
    private void choiceVideo() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, 66);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult VIDEOPATH : " + VIDEOPATH);
        if (requestCode == 66 && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedVideo,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            VIDEOPATH = cursor.getString(columnIndex);
            cursor.close();
            //根据时间路径播放视频
            Log.i(TAG, "VIDEOPATH : " + VIDEOPATH);
/*            showProgressDilog();
            APIService.getInstance().actionRecognition(new OnResultListener() {
                @Override
                public void onResult(Object result) {
                    final String resp = (String) result;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            hideProgressDilog();
                            resultTv.setText(resp);
                            Toast.makeText(ActionActivity.this,resp,Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onError(FaceError error) {

                }
            }, new File(VIDEOPATH));*/
        }
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
    }
}
