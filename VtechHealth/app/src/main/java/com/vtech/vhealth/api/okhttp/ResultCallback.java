package com.vtech.vhealth.api.okhttp;

import android.text.TextUtils;

import com.google.gson.reflect.TypeToken;
import com.vtech.vhealth.function.utils.LogUtil;
import com.zhy.http.okhttp.callback.Callback;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Response;


/**
 * Callback 解析类
 * @date 2017/8/14.
 */
public abstract class ResultCallback<T> extends Callback<T> {

    private static final String TAG = "ResultCallback";
    // 返回的业务Json数据
    private String message = "";
    // 返回code
    private int code = -1;
    // 当result=false时，提示语
    private String msg = "";
    // Json具体业务数据
    private String data = "";
    // 请求URL
    private String requestUrl = "";


    @Override
    public T parseNetworkResponse(Response response, int id) throws Exception {
        try {
            requestUrl = response.request().url().toString();
            LogUtil.show(TAG, "responseCode:" + response.code() + ", url:" + requestUrl);
            this.message = response.body().string();
            LogUtil.show(TAG, "message:" + message);

            JSONObject json = new JSONObject(message);
            this.code = json.optInt("code");
            this.msg = json.optString("msg");

            if (this.code == 0) {
                this.data = json.optString("data");
            }

            Type tp = new TypeToken<String>() {}.getType();
            if (tp.equals(getGenericType(0))) {
                return (T) message;
            }

            if (!TextUtils.isEmpty(data)) {
                return parseResult(data);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public boolean isSuccess() {
        return code==0;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public abstract T parseResult(String result) throws Exception;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * 泛型具体实体Tyle
     * @param index
     * @return
     */
    public Type getGenericType(int index) {
        Type genType = getClass().getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            throw new RuntimeException("Index outof bounds");
        }
        LogUtil.show(this, params[index].toString());
        return params[index];
    }


}
