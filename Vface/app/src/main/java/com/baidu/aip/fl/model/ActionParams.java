package com.baidu.aip.fl.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ActionParams implements RequestParams {

    private Map<String, String> params = new HashMap<>();
    private Map<String, File> fileMap = new HashMap<>();
    private String jsonParams = "";


    @Override
    public Map<String, File> getFileParams() {
        return fileMap;
    }

    @Override
    public Map<String, String> getStringParams() {
        return params;
    }

    @Override
    public String getJsonParams() {
        return jsonParams;
    }

    private String data;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    private void putParam(String key, String value) {
        if (value != null) {
            params.put(key, value);
        } else {
            params.remove(key);
        }
    }

    private void putParam(String key, boolean value) {
        if (value) {
            putParam(key, "true");
        } else {
            putParam(key, "false");
        }
    }
}
