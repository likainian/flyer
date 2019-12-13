package com.flyer.chat.fragment;

import com.alibaba.fastjson.JSON;
import com.flyer.chat.bean.MapUser;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.RetrofitService;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.LogUtil;

import java.util.HashMap;
import java.util.List;

/**
 * Created by mike.li on 2018/11/7.
 */

public class HomePresenter implements HomeContract.HomePresenter {
    private HomeContract.HomeView mView;
    public static final String key = "cb098f1eb5786ce817b33fcf5cecb07c";
    public static final String tableid = "5bfe5ef27bbf195c0724cb46";
    public HomePresenter(HomeContract.HomeView mView) {
        this.mView = mView;
    }

    @Override
    public void getUserInfo() {
    }

    @Override
    public void updateMapUser(String mapId,double latitude,double longitude){
        if(CheckUtil.isEmpty(mapId))return;
        HashMap<String, Object> data = new HashMap<>();
        data.put("_id", mapId);
        data.put("_location",longitude+","+latitude);
        String string = JSON.toJSONString(data);
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("tableid", tableid);
        map.put("loctype", 1);
        map.put("data", string);
        RetrofitService.getInstance().requestFormPost("https://yuntuapi.amap.com/datamanage/data/update", map, new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                LogUtil.i("ttt","更新MapUser"+response);
            }
        });
    }

    @Override
    public void searchMapUser(double latitude,double longitude){
        HashMap<String, Object> map = new HashMap<>();
        map.put("key", key);
        map.put("tableid", tableid);
        map.put("center", longitude+","+latitude);
        map.put("radius", 3000);
        map.put("limit", 100);
        RetrofitService.getInstance().requestFormPost("https://yuntuapi.amap.com/datasearch/around", map, new CallBack<String>() {
            @Override
            public void onResponse(String response) {
                List<MapUser> users = HttpParseUtil.parseArray(response, "datas", MapUser.class);
                LogUtil.i("ttt","搜索："+users.toString());
                mView.showMapUser(users);
            }
        });
    }
}
