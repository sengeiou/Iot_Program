package com.vtech.vhealth.function.main.newpressure;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.vtech.vhealth.R;
import com.vtech.vhealth.function.base.AppDelegate;
import com.vtech.vhealth.function.bean.HealthDTOBean;
import com.vtech.vhealth.function.bean.HealthRecord;
import com.vtech.vhealth.function.main.newpressure.setting.SettingActivity;
import com.vtech.vhealth.function.main.pressure.BpressureFragment;
import com.vtech.vhealth.function.utils.JsonUtil;
import com.vtech.vhealth.function.utils.SpUtils;
import com.vtech.vhealth.function.utils.UriUtils;
import com.vtech.vhealth.function.widget.CircleImageView;

import org.litepal.LitePal;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

public class ACTPressureDelegate extends AppDelegate {
    // 1,2,3,4,5
    private int curUser=1;
    private AnimatorSet animatorSet;

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_pressure;
    }

    public void addClickListener(View.OnClickListener listener) {
        setOnClickListener(listener, R.id.tv_start);
    }

    @Override
    public void initWidget() {
        super.initWidget();
        initRadioGroup();
        initUser();
        initSetting();
        scale();
    }

    public TextView getHint(){
        return get(R.id.tv_hint) ;
    }

    public NEcgView getWave1(){
        return get(R.id.ecg_view_wave1) ;
    }
    public NEcgView getWave2(){
        return get(R.id.ecg_view_wave2) ;
    }
    public NEcgView getWave3(){
        return get(R.id.ecg_view_wave3) ;
    }

    private void scale() {
        final TextView hint=getHint();
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(hint, "scaleX", 1f, 1.2f, 1f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(hint, "scaleY", 1f, 1.2f, 1f);
        animatorSet = new AnimatorSet();
        animatorSet.setDuration(1000);
        objectAnimator.setDuration(1000);//延迟播放
        objectAnimator.setRepeatCount(10000);//重放次数
        objectAnimator.setRepeatMode(ValueAnimator.RESTART);

        objectAnimator2.setDuration(1000);//延迟播放
        objectAnimator2.setRepeatCount(100000);//重放次数
        objectAnimator2.setRepeatMode(ValueAnimator.RESTART);
        animatorSet.playTogether(objectAnimator, objectAnimator2);
    }

    private void  initSetting(){
        setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                SettingActivity.start(getActivity(),curUser);
            }
        },R.id.tv_set);

    }

    public CircleImageView getC1(){
        return get(R.id.ib_user1);
    }
    public CircleImageView getC2(){
        return get(R.id.ib_user2);
    }
    public CircleImageView getC3(){
        return get(R.id.ib_user3);
    }

    public ImageView getIV1(){
        return get(R.id.iv_user1);
    }
    public ImageView getIV2(){
        return get(R.id.iv_user2);
    }
    public ImageView getIV3(){
        return get(R.id.iv_user3);
    }
    public NEcgView getEcg(){
        return get(R.id.ecg_view);
    }

    public NEcgView getWaveEcg(){
        return get(R.id.ecg_view_wave);
    }

    public RadioGroup getRadioGroup(){
        return get(R.id.rg_mod);
    }
    public LinearLayout getDataView(){
        return get(R.id.top_layout);
    }
    public LinearLayout getWaveView(){
        return get(R.id.top_layout2);
    }


    private void initUser(){
        setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.u1:
                        curUser=1;
                        getIV1().setBackgroundResource(R.drawable.user_setect);
                        getIV2().setBackgroundResource(R.drawable.user_nor);
                        getIV3().setBackgroundResource(R.drawable.user_nor);
                        break;
                    case R.id.u2:
                        getIV1().setBackgroundResource(R.drawable.user_nor);
                        getIV2().setBackgroundResource(R.drawable.user_setect);
                        getIV3().setBackgroundResource(R.drawable.user_nor);
                        curUser=2;
                        break;
                    case R.id.u3:
                        getIV1().setBackgroundResource(R.drawable.user_nor);
                        getIV2().setBackgroundResource(R.drawable.user_nor);
                        getIV3().setBackgroundResource(R.drawable.user_setect);
                        curUser=3;
                        break;
                }
                loadHistory();
            }
        },  R.id.u1,R.id.u2,R.id.u3);
        iniUserIcon();
    }

    public void iniUserIcon(){
        initIcon(1,getC1());
        initIcon(2,getC2());
        initIcon(3,getC3());

    }

    private void initIcon(int user ,CircleImageView imageView){
        String userStr = SpUtils.get(SettingActivity.USER_KEY + user, "");
        if (!TextUtils.isEmpty(userStr)) {
            try {
                File path=new File(getActivity().getExternalCacheDir(),user+"outputImage.jpg");
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(UriUtils.getUri(getActivity(),path)));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public int getCurUser(){
        return curUser;
    }


    private void initRadioGroup(){
        RadioGroup group=getRadioGroup();
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                showDataORWave(i);
            }
        });
    }

    public void updateWave(){
        if (getWaveView().getVisibility()==View.VISIBLE) {
            showDataORWave(R.id.tv_mod_wave);
        }
    }

    private void showDataORWave(int i){
        switch (i){
            case R.id.tv_mod_data:
                getDataView().setVisibility(View.VISIBLE);
                getWaveView().setVisibility(View.GONE);
                break;
            case R.id.tv_mod_wave:
                loadHistory();
                getWaveView().setVisibility(View.VISIBLE);
                getDataView().setVisibility(View.GONE);
                break;
        }
    }

    private void loadHistory(){
        List<HealthRecord> healthUploadBeanList = LitePal.where("userId = ? ",
                SettingActivity.USER_KEY+getCurUser()).order("creatTime desc").limit(2).find(HealthRecord.class);
        if (healthUploadBeanList != null) {
            switch (healthUploadBeanList.size()){
                 case 0:
                     clear();
                     break;
                 case 1:
                     getWave2().clearData();
                     getWave3().clearData();
                     HealthRecord record =healthUploadBeanList.get(0);
                     List<Integer> data= JsonUtil.jsonToList(record.getData(),new TypeToken<List<Integer>>(){});
                     getWave2().setGrayColor();
                     getWave3().setGreenColor();
                     getWave2().setData(data);
                     updateData(record);
                     break;
                 case 2:
                     HealthRecord record1 =healthUploadBeanList.get(0);
                     updateData(record1);
                     getWave2().clearData();
                     getWave3().clearData();
                     List<Integer> data1= JsonUtil.jsonToList(record1.getData(),new TypeToken<List<Integer>>(){});
                     getWave2().setGrayColor();
                     getWave2().setData(data1);
                     List<Integer> data2= JsonUtil.jsonToList(healthUploadBeanList.get(1).getData(),new TypeToken<List<Integer>>(){});
                     getWave3().setGreenColor();
                     getWave3().setData(data2);
                     break;
             }
        }else {
           clear();
        }
    }

    private void clear(){
        getWave2().clearData();
        getWave3().clearData();
        HealthRecord healthRecord=new HealthRecord();
        healthRecord.setIhrate(0);
        healthRecord.setIsbp(0);
        healthRecord.setIdbp(0);
        updateData(healthRecord);
    }


    private void updateData(HealthRecord record){
        if (record != null) {
            setText(R.id.tv_sp,String.valueOf(record.getIsbp()));
            setText(R.id.tv_dp,String.valueOf(record.getIdbp()));
            setText(R.id.tv_rate,String.valueOf(record.getIhrate()));
        }
    }
    public void update(List<HealthDTOBean> data){
        if (data != null && data.size()>0) {
            int sp=0;
            int dp=0;
            int rate=0;
            int len =data.size();
            for (int i = 0; i < len; i++) {
                HealthDTOBean healthDTOBean=data.get(i);
              //  LogUtil.show("zjs"," healthDTOBean="+healthDTOBean.toString());
                sp+=healthDTOBean.getiSBP();
                dp+=healthDTOBean.getiDBP();
                rate+=healthDTOBean.getiHRate();
            }
       //   LogUtil.show("zjs"," sp="+sp+" sp/len"+sp/len+" dp="+dp+" dp/len"+dp/len+" rate="+rate+" rate/len"+rate/len  );
            setText(R.id.tv_sp,String.valueOf(sp/len));
            setText(R.id.tv_dp,String.valueOf(dp/len));
            setText(R.id.tv_rate,String.valueOf(rate/len));
        }
    }

    public String getSp(){
        return getTextView(R.id.tv_sp).getText().toString();
    }
    public String getDp(){
        return getTextView(R.id.tv_dp).getText().toString();
    }
    public String getRate(){
        return getTextView(R.id.tv_rate).getText().toString();
    }

    public void updateWave(List<HealthDTOBean> data){
        if (data != null && data.size()>0) {
            HealthDTOBean healthDTOBean=data.get(0);
            setText(R.id.tv_wave_sp,String.valueOf(healthDTOBean.getiSBP()));
            setText(R.id.tv_wave_dp,String.valueOf(healthDTOBean.getiDBP()));
            setText(R.id.tv_wave_rate,String.valueOf(healthDTOBean.getiHRate()));
        }
    }


    public void updateRate(HealthDTOBean rate){
        setText(R.id.tv_rate,String.valueOf(rate.getiHRate()));

    }
    //测量结束后恢复按钮初始状态
    public void reSetTag() {
        TextView start = get(R.id.tv_start);
        if (start == null) {
            return;
        } else {
            start.setText(start.getResources().getString(R.string.str_start));
            start.setTag(BpressureFragment.TAG1);
        }
    }

    public void handlerClickUI(String tag) {
        TextView start = get(R.id.tv_start);
        if (start == null) {
            return;
        }
        switch (tag) {
            case BpressureFragment.TAG1://开始测量
                start.setText(start.getResources().getString(R.string.str_measuring));
                start.setTag(BpressureFragment.TAG2);
                break;
            case BpressureFragment.TAG2:
//                toastShort("正在测量中，请稍候...");
                break;
        }
    }


    public void starAnimator(){
        animatorSet.start();
    }
    public void stopAnimator(){
        animatorSet.cancel();
    }
}
