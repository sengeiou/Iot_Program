package com.vtech.vhealth.function.main.newpressure.setting;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.vtech.vhealth.R;
import com.vtech.vhealth.function.base.AppDelegate;
import com.vtech.vhealth.function.bean.UserBean;
import com.vtech.vhealth.function.utils.ImageLoad;
import com.vtech.vhealth.function.utils.TimeUtil;
import com.vtech.vhealth.function.widget.CustomTitleBar;

import java.io.File;

public class SettingDelegate extends AppDelegate {

    @Override
    public int getRootLayoutId() {
        return R.layout.activity_user_setting;
    }


    @Override
    public void initWidget() {
        super.initWidget();
        initTitle();
        initTime();
    }


    private void initTime() {
        setText(R.id.tv_time, TimeUtil.getTime()+"     "+TimeUtil.getAmPm());
        setText(R.id.tv_date, TimeUtil.getDay());

    }

    public void initUser(UserBean user,int curUser) {
        try{
            if (user != null) {
                getName().setText(user.getUserName());
                getAge().setText(String.valueOf(user.getAge()));
                getArea().setText(user.getArea());
                getSex().setSelection(Integer.parseInt(user.getSex()));
                getID().setText(user.getUid());
                String url=user.getIcon();
                if (TextUtils.isEmpty(url)) {
                    File path=new File(getActivity().getExternalCacheDir(),curUser+"outputImage.jpg");
                    url=path.getAbsolutePath();
                }
                ImageLoad.load(getActivity().getApplicationContext(),url,getIcon());
            }else {
                File path=new File(getActivity().getExternalCacheDir(),curUser+"outputImage.jpg");
                ImageLoad.load(getActivity().getApplicationContext(),path.getAbsolutePath(),getIcon());
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    private void initTitle(){
        CustomTitleBar titleBar = getTitle();
        titleBar.setTextCenter(getRootView().getContext().getText(R.string.str_set));
        titleBar.setOnClickLeftViewListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity=getActivity();
                if (activity != null) {
                    activity.finish();
                }
            }
        });
    }

    public void addClickListener(View.OnClickListener listener) {
        setOnClickListener(listener, R.id.tv_ok,R.id.tv_cancle,R.id.iv_icon);
    }
    public CustomTitleBar getTitle() {
        return get(R.id.title_bar);
    }

    public ImageView getIcon(){
        return get(R.id.iv_icon) ;
    }


    public EditText getName(){
        return get(R.id.et_name) ;
    }
    public EditText getAge(){
        return get(R.id.et_age) ;
    }
    public Spinner getSex(){
        return get(R.id.et_sex) ;
    }
    public EditText getID(){
        return get(R.id.et_id) ;
    }
    public EditText getArea(){
        return get(R.id.et_area) ;
    }

    public UserBean getUserBean() {
        UserBean userBean = new UserBean();
        String name = getName().getText().toString().trim();
        String age = getAge().getText().toString().trim();
        String id = getID().getText().toString().trim();
        String sex =""+getSex().getSelectedItemPosition();
        String area = getArea().getText().toString().trim();

        userBean.setUserName(name);
        if (TextUtils.isEmpty(age)) {
            userBean.setAge(0);
        }else {
            userBean.setAge(Integer.valueOf(age));
        }

        userBean.setArea(area);
        userBean.setUid(id);
        userBean.setSex(sex);

        return userBean;
    }

}
