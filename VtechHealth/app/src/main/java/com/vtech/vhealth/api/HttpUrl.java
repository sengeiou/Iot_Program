package com.vtech.vhealth.api;

public class HttpUrl {


    protected static String BASEURL = "http://47.112.217.188/api/v1/";

    protected static String PUSH_HEALTH =BASEURL+"heartrate";
   //java 接口
    protected static String PUSH_ECG_DATA ="http://47.112.217.188:8999/spo/ecg/insert.do";

    protected static String PUSH_DEVICESER =BASEURL+"deviceuser";

    protected static String PUSH_USERICON =BASEURL+"upload";



}
