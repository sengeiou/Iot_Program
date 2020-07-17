package com.baidu.aip.fl.parser;

import android.text.TextUtils;
import android.util.Log;

import com.baidu.aip.fl.exception.FaceError;
import com.baidu.aip.fl.model.ActionResult;
import com.baidu.aip.fl.model.FaceModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionParser implements Parser<ActionResult> {
    @Override
    public ActionResult parse(String json) throws FaceError {
        Log.d("ActionResult", json);
        ActionResult actionResult = null;
        try {
            JSONObject jsonObject = new JSONObject(json);
            int errorCode = jsonObject.optInt("error_code");
            if (errorCode > 0) {
                String errorMsg = jsonObject.optString("error_msg");
                FaceError faceError = new FaceError(errorCode, errorMsg);
                throw faceError;
            }
            JSONArray resultArray = jsonObject.optJSONArray("result");
            actionResult = new ActionResult();

            if (resultArray != null) {
                actionResult.setScore(resultArray.getDouble(0));
            }
            actionResult.setName(jsonObject.optString("name"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }
}
