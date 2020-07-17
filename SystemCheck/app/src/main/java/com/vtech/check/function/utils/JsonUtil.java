package com.vtech.check.function.utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

public class JsonUtil {

    private static  Gson mGson=new Gson();
    /**
     * 对象转成 json 字符串
     * **/
    public static String beanToJson(Object bean) {
        String jsonStr = mGson.toJson(bean);
        return jsonStr;
    }
    public static <T> T jsonToBean(String json, Class<T> clazz) {
        return  mGson.fromJson(json,clazz);
    }

    public static <T> List<T> jsonToList(String json, TypeToken<List<T>> clazz) {
//        List<Person> statusLs = gson.fromJson(result, new TypeToken<List<Person>>(){}.getType());
        return  mGson.fromJson(json,clazz.getType());
    }


    public static void main(String[] args) {
        byte[] mBuffer= {(byte) (0xf8 & 0xff), 0x20, 0x01, 0x02};
        String tail = "ab";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length + tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);

        for (int i = 0; i < mBuffer.length; i++) {
            System.out.printf("====["+i+"]="+mBufferTemp[i]);
        }

    }
}
