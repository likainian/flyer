package com.flyer.chat.network;

import com.alibaba.fastjson.JSONObject;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by mike.li on 2018/5/2.
 */

public class PostRequestBody {
    private JSONObject json = new JSONObject();
    public PostRequestBody put(String key, Object value){
        json.put(key,value);
        return this;
    }
    public RequestBody build(){
        return RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toJSONString());
    }
}
