package com.vtech.app.util;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.vtech.app.App;
import com.vtech.app.view.LoadingBar;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by admin on 2019/6/14.
 * <p>
 * 模仿Async-Http通信库写出的okhttp子线程跨主线程通信类
 * <p>
 * 主要参考AsyncHttpClient、AsyncHttpResponseHandler类
 * 参考Retofit2中OkHttpCall类、okhttp3中RealCall类
 * 主要采用了适配器模式、模板模式
 * <p>
 * loadingBar 加载框
 */

public class OkHttpUtil {
    private static final String TAG = "OkHttpUtil";
    private static LoadingBar loadingBar;
    private static volatile boolean isSetLoading = false;
    private static OkHttpClient client;

    static {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY);
        client = new OkHttpClient.Builder().writeTimeout(30 * 1000, TimeUnit.MILLISECONDS)
                .readTimeout(20 * 1000, TimeUnit.MILLISECONDS)
                .connectTimeout(15 * 1000, TimeUnit.MILLISECONDS)
                //设置拦截器，显示日志信息
                .addNetworkInterceptor(httpLoggingInterceptor)
                .retryOnConnectionFailure(true)
                .build();
    }

    private OkHttpUtil() {
    }

    public static OkHttpClient getClient() {
        return client;
    }

    public static void post(final Context ctx, String url, String jsonStr, final SimpleResponseHandler responseHandler) {
        isSetLoading = true;
        loadingBar = new LoadingBar(ctx);
        Logger.d("post", "url:" + url + "\njsonStr:" + jsonStr);
        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, jsonStr.toString()))
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        getDefaultThreadPool().execute(new ResponseRunnable(call, responseHandler));
    }

    public static void postNoLoading(Context ctx, String url, String jsonStr, final SimpleResponseHandler responseHandler) {
        isSetLoading = false;
        Logger.d("post", "url:" + url + "\njsonStr:" + jsonStr);
//        MediaType mediaType = MediaType.parse("text/x-markdown; charset=utf-8");
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        Request request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(mediaType, jsonStr))
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        getDefaultThreadPool().execute(new ResponseRunnable(call, responseHandler));
    }

    /**
     * 表单请求参数
     *
     * @param ctx
     * @param url
     * @param formBody
     * @param responseHandler //创建表单请求参数
     *                        FormBody.Builder builder = new FormBody.Builder();
     *                        builder.add("cityName", cityName);
     *                        FormBody formBody = builder.build();
     */
    public static void postFormNoLoading(String url, FormBody formBody, final SimpleResponseHandler responseHandler) {
        isSetLoading = false;
        Logger.d("postFormNoLoading", "url:" + url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("language", App.getInstance().getLang() + "")
                .post(formBody)
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        getDefaultThreadPool().execute(new ResponseRunnable(call, responseHandler));
    }

    public static void postFormWithToken(String url, FormBody formBody, final SimpleResponseHandler responseHandler) {
        Log.i(TAG, "App token : " + App.getInstance().getToken() + " , Preference token : " + PreferenceUtils.getPrefString(App.getInstance(), PreferenceConstants.ACCESS_TOKEN, ""));
        isSetLoading = false;
        Logger.d("postFormNoLoading", "url:" + url);
        Request request = new Request.Builder()
                .url(url)
                .addHeader("language", App.getInstance().getLang() + "")
                .addHeader("token", App.getInstance().getToken())
                .post(formBody)
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        getDefaultThreadPool().execute(new ResponseRunnable(call, responseHandler));
    }

    public static void get(String url, final SimpleResponseHandler responseHandler) {
        Request request = new Request.Builder()
                .get()
                .url(url)
                .build();
//        client.newCall(request).enqueue(responseHandler);
        Call call = client.newCall(request);
        getDefaultThreadPool().execute(new ResponseRunnable(call, responseHandler));
    }

    private static ExecutorService getDefaultThreadPool() {
        return Executors.newCachedThreadPool();
    }

    private static class ResponseRunnable implements Runnable {
        private Call call;
        private SimpleResponseHandler callback;

        public ResponseRunnable(Call call, SimpleResponseHandler callback) {
            this.call = call;
            this.callback = callback;
            callback.sendStartMessage();
        }

        @Override
        public void run() {
            try {
                Response response = call.execute();
                callback.onResponse(call, response);
            } catch (IOException e) {
                callback.onFailure(call, e);
            } finally {
                callback.sendFinishMessage();
            }
        }
    }

    /**
     * 模板模式-----定义算法的步骤，并把这些实现延迟到子类
     */
    public abstract static class SimpleResponseHandler implements Callback {
        private Handler handler;

        public SimpleResponseHandler() {
            Looper looper = Looper.myLooper();
            this.handler = new ResultHandler(this, looper);
        }

        public void handleMessage(Message message) {
            Log.d(TAG, "SimpleResponseHandler  handleMessage current Thread: " + Thread.currentThread().getName() + ", message.what() == " + message.what);
            switch (message.what) {
                case -1:
                    onStart();
                    break;
                case 0:
                    Object[] objects = (Object[]) message.obj;
                    onSuccess((Call) objects[0], (Response) objects[1]);
                    break;
                case 1:
                    onFailure((Exception) message.obj);
                    break;
                case 2:
                    onFinish();
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.i(TAG, "SimpleResponseHandler   onResponse current Thread: " + Thread.currentThread().getName() + " , ThreadId : " + Thread.currentThread().getId());
            if (response.code() < 200 || response.code() >= 300) {
                sendFailuerMessage(new IOException(response.message()));
            } else {
                sendSuccessMessage(response.code(), call, response);
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            Log.i(TAG, "SimpleResponseHandler   onFailure current Thread: " + Thread.currentThread().getName() + " , ThreadId : " + Thread.currentThread().getId());
            sendFailuerMessage(e);
        }

        void sendStartMessage() {
            this.handler.sendMessage(obtainMessage(-1, null));
        }

        void sendSuccessMessage(int code, Call call, Response response) {
            this.handler.sendMessage(obtainMessage(0, new Object[]{call, response}));
        }

        void sendFailuerMessage(Throwable throwable) {
            this.handler.sendMessage(obtainMessage(1, throwable));
        }

        void sendFinishMessage() {
            this.handler.sendMessage(obtainMessage(2, null));
        }

        Message obtainMessage(int responseMessageId, Object responseMessageData) {
            return Message.obtain(this.handler, responseMessageId, responseMessageData);
        }

        public void onStart() {
            Log.d(TAG, "SimpleResponseHandler    onStart");
            if (isSetLoading) {
                loadingBar.show();
            }
        }

        public void onFinish() {
            Log.d(TAG, "SimpleResponseHandler    onFinish");
            if (isSetLoading || (loadingBar != null && loadingBar.isShowing())) {
                loadingBar.cancel();
            }
        }

        public abstract void onSuccess(Call call, Response response);

        public abstract void onFailure(Exception error);
    }

    /**
     * 类适配器模式------将一个类的接口，转换为客户期盼的另一个接口。通过继承的方式.
     */
    private static class ResultHandler extends Handler {
        SimpleResponseHandler responseHandler;

        ResultHandler(SimpleResponseHandler handler, Looper looper) {
            super(looper);
            this.responseHandler = handler;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            responseHandler.handleMessage(msg);
        }
    }
}