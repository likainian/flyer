package com.flyer.chat.network;

import com.flyer.chat.util.LogUtil;
import com.mob.imsdk.MobIMCallback;

/**
 * Created by mike.li on 2018/9/12.
 */

public class IMCallback<T> implements MobIMCallback<T> {
    @Override
    public void onSuccess(T t) {
        LogUtil.i("im","imOnSuccess"+t);
    }

    @Override
    public void onError(int i, String s) {
        LogUtil.i("im","imOnError"+s);
    }
}
