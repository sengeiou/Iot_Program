package com.vtech.app.data;

import android.content.Context;
import android.util.Log;

import com.vtech.app.App;
import com.vtech.app.util.OkHttpUtil;
import com.vtech.app.util.PreferenceConstants;
import com.vtech.app.util.PreferenceUtils;
import com.vtech.app.util.Utils;
import com.vtech.app.util.XmlUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.Response;

public class APIService {
    public static final String TAG = "APIService";
    private static final String BASE_URL = "http://47.112.217.188:8999";

    private static final String REGIST_DEVICE = BASE_URL + "/spo/device/adddevice.do";

    private static final String GET_WEATHER = BASE_URL + "/spo/weather/findweatherbyname.do";

    private APIService() {

    }

    private static volatile APIService instance;

    public static APIService getInstance() {
        if (instance == null) {
            synchronized (APIService.class) {
                if (instance == null) {
                    instance = new APIService();
                }
            }
        }
        return instance;
    }

    public void init(Context context) {
        // 采用deviceId分组
//        HttpUtil.getInstance().init();
//        DeviceUuidFactory.init(context);
    }

    public void getWether(String cityName, OkHttpUtil.SimpleResponseHandler simpleResponseHandler) {
        //创建表单请求参数
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("cityName", cityName);
        FormBody formBody = builder.build();
        OkHttpUtil.postFormWithToken(GET_WEATHER, formBody, simpleResponseHandler);
    }

    public void registDevice(final Context context) {
        String imei = Utils.getDeviceId(context);
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("deviceId", imei);
        FormBody formBody = builder.build();
        OkHttpUtil.postFormNoLoading(REGIST_DEVICE, formBody, new OkHttpUtil.SimpleResponseHandler() {
            @Override
            public void onSuccess(Call call, Response response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    int code = jsonObject.optInt("code");
                    String data = jsonObject.optString("data");
                    App.getInstance().setToken(data);
                    PreferenceUtils.setPrefString(App.getInstance(),PreferenceConstants.ACCESS_TOKEN,data);
                    XmlUtils.wirteXmlConfig(data);
                    if (code == 0) {
                        Log.i(TAG, "registDevice success");
                    } else {
                        Log.w(TAG, "registDevice fail : " + jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception error) {
                Log.e(TAG, "registDevice fail : " + error.toString());
            }
        });
    }

}
