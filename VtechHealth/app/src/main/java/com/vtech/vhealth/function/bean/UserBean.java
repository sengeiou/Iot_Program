package com.vtech.vhealth.function.bean;

import org.litepal.crud.LitePalSupport;

public class UserBean extends LitePalSupport {


    public String icon;
    public String userId;
    public String userName;
    public int age ;
    public String sex;
    public String uid;//与机主身份关系 如父母兄弟
    public String area;//所在地
    public String creatTime;//创建时间 暂定

    public String getUserId() {
        return userId;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getCreatTime() {
        return creatTime;
    }

    public void setCreatTime(String creatTime) {
        this.creatTime = creatTime;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "icon='" + icon + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", age=" + age +
                ", sex='" + sex + '\'' +
                ", uid='" + uid + '\'' +
                ", area='" + area + '\'' +
                ", creatTime='" + creatTime + '\'' +
                '}';
    }
}
