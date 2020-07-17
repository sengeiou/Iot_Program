/*
 * Copyright (C) 2019 Baidu, Inc. All Rights Reserved.
 */
package com.baidu.aip.fl.parser;

import android.util.Log;

import com.baidu.aip.fl.exception.FaceError;
import com.baidu.aip.fl.model.FaceListResult;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class FaceListParser implements Parser<FaceListResult> {
    @Override
    public FaceListResult parse(String json) throws FaceError {
        Log.e("FaceListParser", "oarse:" + json);
        try {
            JSONObject jsonObject = new JSONObject(json);

            if (jsonObject.has("error_code")) {
                FaceError error = new FaceError(jsonObject.optInt("error_code"), jsonObject.optString("error_msg"));
                if (error.getErrorCode() != 0) {
                    throw error;
                }
            }

            JSONObject rjson = jsonObject.optJSONObject("result");

            FaceListResult result = new Gson().fromJson(rjson.toString(), FaceListResult.class);

            return result;
        } catch (JSONException e) {
            e.printStackTrace();
            FaceError error = new FaceError(FaceError.ErrorCode.JSON_PARSE_ERROR, "Json parse error:" + json, e);
            throw error;
        }
    }
}
