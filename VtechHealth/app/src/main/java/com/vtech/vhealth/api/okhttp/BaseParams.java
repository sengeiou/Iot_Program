package com.vtech.vhealth.api.okhttp;


import com.vtech.vhealth.AppData;
import com.vtech.vhealth.function.utils.TokenUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;

/**
 * 类说明：封装公共参数到请求中的api，用于返回其他api的调用的HTTP PostFormBuilder
 */
public class BaseParams {
    /**
     * 封装公共参数的PostFormBuilder 返回
     *
     *   token
     *  language    语言 0简体 1繁体 2英文 必须填
     * @return
     */
    public static PostFormBuilder getPostBuilder() {
        return OkHttpUtils.post()
                .addHeader("language",String.valueOf(TokenUtils.getLangugeType(AppData.get())))
                .addHeader("token", TokenUtils.getToken(AppData.get()))
                .addHeader("Content-Type","application/x-www-form-urlencoded");
    }

}
