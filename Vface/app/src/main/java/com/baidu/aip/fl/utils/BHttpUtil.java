package com.baidu.aip.fl.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * http 工具类
 */
public class BHttpUtil {

    public static String post(String requestUrl, String accessToken, String params, OnResultListener listener)
            throws Exception {
        String contentType = "application/x-www-form-urlencoded";
        return BHttpUtil.post(requestUrl, accessToken, contentType, params, listener);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, OnResultListener listener)
            throws Exception {
        String encoding = "UTF-8";
        if (requestUrl.contains("nlp")) {
            encoding = "GBK";
        }
        return BHttpUtil.post(requestUrl, accessToken, contentType, params, encoding, listener);
    }

    public static String post(String requestUrl, String accessToken, String contentType, String params, String encoding, OnResultListener listener)
            throws Exception {
        String url = requestUrl + "?access_token=" + accessToken;
        return BHttpUtil.postGeneralUrl(url, contentType, params, encoding, listener);
    }

    public static String postGeneralUrl(String generalUrl, String contentType, String params, String encoding, OnResultListener listener)
            throws Exception {
        long start = System.currentTimeMillis();
        System.err.println("url --->" + generalUrl);
        URL url = new URL(generalUrl);
        // 打开和URL之间的连接
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST");
        // 设置通用的请求属性
        connection.setRequestProperty("Content-Type", contentType);
        connection.setRequestProperty("Connection", "Keep-Alive");
        connection.setUseCaches(false);
        connection.setDoOutput(true);
        connection.setDoInput(true);

        // 得到请求的输出流对象
        DataOutputStream out = new DataOutputStream(connection.getOutputStream());
        out.write(params.getBytes(encoding));
        out.flush();
        out.close();

        // 建立实际的连接
        connection.connect();
        // 获取所有响应头字段
        Map<String, List<String>> headers = connection.getHeaderFields();
        // 遍历所有的响应头字段
        for (String key : headers.keySet()) {
            System.err.println(key + "--->" + headers.get(key));
        }
        // 定义 BufferedReader输入流来读取URL的响应
        BufferedReader in = null;
        in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), encoding));
        String result = "";
        String getLine;
        while ((getLine = in.readLine()) != null) {
            result += getLine;
        }
        in.close();
        System.err.println("result:" + result);
        long end = System.currentTimeMillis();
        System.err.println("BHttpUtil end time:" + (end - start));

        listener.onResult(result);
        return result;
    }
}
