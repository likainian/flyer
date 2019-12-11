package com.flyer.chat.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.mob.imsdk.model.IMMessage;

/**
 * Created by mike.li on 2018/8/30.
 */

public class MessageItem implements MultiItemEntity{
    public static final int LEFT_TEXT = 11;
    public static final int RIGHT_TEXT = 12;
    private IMMessage message;

    public MessageItem() {
    }

    public MessageItem(IMMessage message) {
        this.message = message;
    }

    @Override
    public int getItemType() {
        if(message.getFrom().equals(SharedPreferencesUtil.getInstance().getMobileNo())){
            return RIGHT_TEXT;
        }else {
            return LEFT_TEXT;
        }
    }

    public IMMessage getMessage() {
        return message;
    }

    public void setMessage(IMMessage message) {
        this.message = message;
    }
}
