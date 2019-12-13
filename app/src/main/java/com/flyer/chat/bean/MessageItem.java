package com.flyer.chat.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Created by mike.li on 2018/8/30.
 */

public class MessageItem implements MultiItemEntity{
    public static final int LEFT_TEXT = 11;
    public static final int RIGHT_TEXT = 12;

    public MessageItem() {
    }


    @Override
    public int getItemType() {
        return 0;
    }


}
