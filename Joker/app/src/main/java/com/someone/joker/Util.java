package com.someone.joker;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Anonymous on 2016/6/5.
 */
public class Util {

    public static final String TAG = "Util";

    /**
     * 解析Json
     * @param json 原始Json
     * @return 数据源
     */
    public List<String> parseJson(String json){

        List<String> dataList = new ArrayList<String>();

        try {
            // 先取出result对应的Json字符串
            JSONObject firstJson = new JSONObject(json);
            int errorCode = firstJson.getInt("error_code");
            String firstResult = null;
            if (errorCode == 0){
                firstResult = firstJson.getString("result");
            }
//            Log.d(TAG, "firstResult: " + firstResult);

            // 取出result中data对应的Json字符串
            JSONObject secondJson = new JSONObject(firstResult);
            String secondResult = null;
            secondResult = secondJson.getString("data");
//            Log.d(TAG, "secondResult: " + secondResult);

            // 遍历查询每个Json对象中的content对应的值
            JSONArray jsonArray = new JSONArray(secondResult);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String content = jsonObject.getString("content");
//                Log.d(TAG, "content: " + content);
                dataList.add(content);
            }
            Log.d(TAG, "dataList: " + dataList.size());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return dataList;
    }

}
