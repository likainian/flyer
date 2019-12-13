package com.flyer.chat.map;

import com.alibaba.fastjson.JSON;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.HttpParseUtil;

import java.util.HashMap;

/**
 * Created by mike.li on 2018/11/29.
 */

public class MapCloud {
    public static final String key = "cb098f1eb5786ce817b33fcf5cecb07c";
    public static final String tableId = "5bfe5ef27bbf195c0724cb46";
    public static void CreateMapUser(User user, final CallBack<String> callBack){
        HashMap<String, Object> data = new HashMap<>();
        data.put("_name","");
        data.put("_location","104.394729,31.125698");
        String string = JSON.toJSONString(data);
        final HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("tableid", tableId);
        map.put("loctype", 1);
        map.put("data", string);
        RetrofitService.getInstance().requestFormPost("https://yuntuapi.amap.com/datamanage/data/create", map, new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                String info = HttpParseUtil.parseString(response, "info");
                String id = HttpParseUtil.parseString(response, "_id");
                if("OK".equals(info)){
                    callBack.onResponse(id);
                }
            }
        });
    }

    public static void getMapUser(String id,CallBack<String> callBack){
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("tableid", tableId);
        map.put("_id", id);
        RetrofitService.getInstance().requestGet("https://yuntuapi.amap.com/datasearch/id", map, callBack);
    }

    public void updateMapUser(String mapId,double latitude,double longitude,CallBack<String> callBack){
        if(CheckUtil.isEmpty(mapId))return;
        HashMap<String, Object> data = new HashMap<>();
        data.put("_id", mapId);
        data.put("_location",longitude+","+latitude);
        String string = JSON.toJSONString(data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("tableid", tableId);
        map.put("loctype", 1);
        map.put("data", string);
        RetrofitService.getInstance().requestFormPost("https://yuntuapi.amap.com/datamanage/data/update", map, callBack);
    }

    public static void updateMapUser(String mapId,String address,CallBack<String> callBack){
        if(CheckUtil.isEmpty(mapId))return;
        HashMap<String, Object> data = new HashMap<>();
        data.put("_id", mapId);
        data.put("_location","104.394729,31.125698");
        data.put("_address",address);
        String string = JSON.toJSONString(data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("tableid", tableId);
        map.put("loctype", 1);
        map.put("data", string);
        RetrofitService.getInstance().requestFormPost("https://yuntuapi.amap.com/datamanage/data/update", map, callBack);
    }
    public static void searchMapUser(double latitude,double longitude,CallBack<String> callBack){
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("tableid", tableId);
        map.put("center", longitude+","+latitude);
        map.put("radius", 3000);
        map.put("limit", 100);
        RetrofitService.getInstance().requestFormPost("https://yuntuapi.amap.com/datasearch/around", map, callBack);
    }
}
