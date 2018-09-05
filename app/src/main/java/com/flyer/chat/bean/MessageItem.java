package com.flyer.chat.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.flyer.chat.adapter.ConversationAdapter;

import cn.jpush.im.android.api.enums.MessageDirect;
import cn.jpush.im.android.api.model.Message;

/**
 * Created by mike.li on 2018/8/30.
 */

public class MessageItem implements MultiItemEntity{
    private Message message;

    public MessageItem() {
    }

    public MessageItem(Message message) {
        this.message = message;
    }

    @Override
    public int getItemType() {
        if(message.getDirect()== MessageDirect.receive){
            return ConversationAdapter.LEFT_TEXT;
        }else if(message.getDirect()== MessageDirect.send){
            return ConversationAdapter.RIGHT_TEXT;
        }
        return 0;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
