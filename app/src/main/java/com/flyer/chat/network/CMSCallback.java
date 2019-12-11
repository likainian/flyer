package com.flyer.chat.network;

import com.flyer.chat.util.LogUtil;
import com.mob.cms.Callback;

/**
 * Created by mike.li on 2018/11/8.
 */

public class CMSCallback<T> extends Callback<T>{
    @Override
    public void onSuccess(T t) {
        LogUtil.i("cms",t.toString());
        super.onSuccess(t);
    }

    @Override
    public void onFailed(Throwable throwable) {
        LogUtil.i("cms",throwable.getMessage());
        super.onFailed(throwable);
    }

    @Override
    public void onCancel() {
        LogUtil.i("cms","onCancel");
        super.onCancel();
    }
}
