package com.flyer.chat.util;

import android.widget.Toast;

import com.flyer.chat.app.ChatApplication;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;


public class ToastHelper {
    private static Toast toast;
    private static String oldMsg;
    private static long time;
    public static void showToast(final String message) {
        if (CheckUtil.isEmpty(message)) return;
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(ObservableEmitter<Object> e) throws Exception {
                if (!message.equals(oldMsg)) {
                    if(toast!=null) {
                        toast.cancel();
                    }
                    toast = Toast.makeText(ChatApplication.getInstance(), message, Toast.LENGTH_LONG);
                    toast.show();
                    time = System.currentTimeMillis();
                } else {
                    // 显示内容一样时，只有间隔时间大于2秒时才显示
                    if (System.currentTimeMillis() - time > 2000) {
                        if(toast!=null) {
                            toast.cancel();
                        }
                        toast = Toast.makeText(ChatApplication.getInstance(), message, Toast.LENGTH_LONG);
                        toast.show();
                        time = System.currentTimeMillis();
                    }
                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe();
        oldMsg = message;
    }
}
