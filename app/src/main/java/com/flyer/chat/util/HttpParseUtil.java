package com.flyer.chat.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mike.li on 2018/4/20.
 */

public class HttpParseUtil {

    public static <T> T parseObject(String json, Class<T> clazz) {
        T t = null;
        if (json!=null) {
            try {
                t = JSON.parseObject(json, clazz);
            }catch (Exception e){
                return null;
            }

        }
        return t;
    }

    public static <T> T parseObject(String json, String paramString, Class<T> clazz) {
        T t = null;
        if (null != paramString) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.containsKey(paramString)) {
                String data = jsonObject.getString(paramString);
                t = JSON.parseObject(data, clazz);
            }
        }
        return t;
    }

    public static <T> List<T> parseArray(String json, Class<T> clazz) {
        return JSON.parseArray(json, clazz);
    }

    public static <T> List<T> parseArray(String json, String paramString, Class<T> clazz) {
        List<T> list = new ArrayList<>();
        if (null != paramString) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.containsKey(paramString)) {
                String data = jsonObject.getString(paramString);
                list = JSON.parseArray(data, clazz);
            }
        }
        return list;
    }

    public static boolean parseBoolean(String json, String paramString) {
        boolean result = false;
        if (paramString != null) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.containsKey(paramString)) {
                result = jsonObject.getBoolean(paramString);
            }
        }
        return result;
    }

    public static String parseString(String json, String paramString) {
        String result = null;
        if (paramString != null) {
            JSONObject jsonObject = JSONObject.parseObject(json);
            if (jsonObject.containsKey(paramString)) {
                result = jsonObject.getString(paramString);
            }
        }
        return result;
    }
}
