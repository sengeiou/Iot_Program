package com.vtech.vhealth.api.okhttp;


import com.google.gson.Gson;


/**
 * 业务数据解析类
 */
public abstract class BodyCallback<T> extends ResultCallback<T> {


    @Override
    public T parseResult(String result) throws Exception {
        return new Gson().fromJson(result, getGenericType(0));
    }

}