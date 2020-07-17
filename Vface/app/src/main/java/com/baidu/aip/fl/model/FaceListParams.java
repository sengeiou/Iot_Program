/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.fl.model;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class FaceListParams implements RequestParams {

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

    private void putParam(String key, String value) {
        if (value != null) {
            params.put(key, value);
        } else {
            params.remove(key);
        }
    }

    public void setUserId(String userId) {
        putParam("user_id", userId);
    }

    public void setGroupId(String groupId) {
        putParam("group_id", groupId);
    }

    public void setFaceToken(String face_token){
        putParam("face_token", face_token);
    }

    private void putParam(String key, boolean value) {
        if (value) {
            putParam(key, "true");
        } else {
            putParam(key, "false");
        }
    }
}
