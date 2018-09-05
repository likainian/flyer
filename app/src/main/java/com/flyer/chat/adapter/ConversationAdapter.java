package com.flyer.chat.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.bean.MessageItem;
import com.google.gson.jpush.JsonElement;
import com.google.gson.jpush.JsonObject;

import java.util.ArrayList;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by mike.li on 2018/8/14.
 */

public class ConversationAdapter extends BaseMultiItemQuickAdapter<MessageItem,BaseViewHolder> {
    public static final int LEFT_TEXT = 11;
    public static final int RIGHT_TEXT = 12;
    public ConversationAdapter() {
        super(new ArrayList<MessageItem>());
        addItemType(LEFT_TEXT,R.layout.item_conversation_left_text);
        addItemType(RIGHT_TEXT,R.layout.item_conversation_right_text);
    }

    @Override
    protected void convert(BaseViewHolder helper, MessageItem item) {
        Message message = item.getMessage();
        JsonElement jsonElement = message.getContent().toJsonElement();
        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        JsonElement text = asJsonObject.get("text");
        helper.setText(R.id.text,text.toString());
    }
}
