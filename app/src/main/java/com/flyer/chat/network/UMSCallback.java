package com.flyer.chat.network;

import android.content.Intent;

import com.flyer.chat.activity.account.LoginActivity;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.bean.UmCode;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.LogUtil;
import com.mob.ums.OperationCallback;
import com.flyer.chat.util.ToastUtil;
import com.mob.ums.User;

/**
 * Created by mike.li on 2018/9/10.
 */

public class UMSCallback<T> extends OperationCallback<T>{
    @Override
    public void onSuccess(T t) {
        LogUtil.i("ums","onSuccess");
        if(t instanceof User){
            User user = (User) t;
            if(CheckUtil.isEmpty(user.id.get())){
                Intent intent = new Intent(ChatApplication.getInstance(),LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ChatApplication.getInstance().startActivity(intent);
            }
        }
        super.onSuccess(t);
    }

    @Override
    public void onFailed(Throwable throwable) {
        LogUtil.i("ums",throwable.toString()+throwable.getMessage());
        try {
            UmCode umCode = HttpParseUtil.parseObject(throwable.getMessage(), UmCode.class);
            if(CheckUtil.isNotEmpty(umCode)&&CheckUtil.isNotEmpty(umCode.getDetail())){
                ToastUtil.showToast(umCode.getDetail());
            }
        }catch (Exception e){
            ToastUtil.showToast(throwable.getMessage());
        }

        super.onFailed(throwable);
    }

    @Override
    public void onCancel() {
        LogUtil.i("ums","onCancel");
        super.onCancel();
    }
}
