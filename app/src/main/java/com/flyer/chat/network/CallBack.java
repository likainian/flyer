package com.flyer.chat.network;


import com.flyer.chat.util.ToastHelper;

/**
 * Created by mike.li on 2018/5/15.
 */

public abstract class CallBack<T> {
    public abstract void onResponse(T response);
    public void onError(String message){
        ToastHelper.showToast(message);
    }
}
