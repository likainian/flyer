package com.flyer.chat.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.bean.MessageItem;
import com.mob.imsdk.model.IMMessage;

import java.util.ArrayList;

/**
 * Created by mike.li on 2018/8/14.
 */

public class ConversationAdapter extends BaseMultiItemQuickAdapter<MessageItem,BaseViewHolder> {
    public ConversationAdapter() {
        super(new ArrayList<MessageItem>());
        addItemType(MessageItem.LEFT_TEXT,R.layout.item_conversation_left_text);
        addItemType(MessageItem.RIGHT_TEXT,R.layout.item_conversation_right_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageItem item) {
        IMMessage message = item.getMessage();
        helper.setText(R.id.text,message.getBody());
    }
}
