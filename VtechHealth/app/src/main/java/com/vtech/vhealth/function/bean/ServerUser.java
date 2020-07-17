package com.vtech.vhealth.function.bean;

public class ServerUser {


    /**
     * device_id : SP0100MA4WB2800000001
     * device_user_id : user_key1
     * name : 刚手机1
     * age : 223
     * sex : 0
     * address : 你公公
     * identity : 公公
     * portrait : portrait/20200513/90a5db05d2b08c8b5de4c18bf39b1c92.jpg
     */

    private String device_id;
    private String device_user_id;
    private String name;
    private int age;
    private int sex;
    private String address;
    private String identity;
    private String portrait;

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getDevice_user_id() {
        return device_user_id;
    }

    public void setDevice_user_id(String device_user_id) {
        this.device_user_id = device_user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getPortrait() {
        return portrait;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }
}
