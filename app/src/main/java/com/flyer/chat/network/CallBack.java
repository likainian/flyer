package com.flyer.chat.network;

import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.BuildConfig;
import com.flyer.chat.util.LogUtil;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by mike.li on 2018/5/15.
 */

public abstract class CallBack<T> {
    public abstract void onResponse(T response);
    public void onError(String message) {
        ToastUtil.showToast(message);
    }
    public void onProgress(long percent){
        LogUtil.i("percent:"+percent);
    }
    public void onError(Throwable throwable){
        if(BuildConfig.DEBUG){
            throwable.printStackTrace();
        }
        if(throwable instanceof UnknownHostException) {
            ToastUtil.showToast("未找到网路请求路径");
        }else if(throwable instanceof ConnectException){
            ToastUtil.showToast("服务器连接异常，请重试！");
        }else if(throwable instanceof SocketTimeoutException){
            ToastUtil.showToast("连接超时");
        }else if(throwable instanceof com.alibaba.fastjson.JSONException){
            ToastUtil.showToast("解析数据异常");
        }else {
            ToastUtil.showToast(throwable.getMessage());
        }
    }
}
