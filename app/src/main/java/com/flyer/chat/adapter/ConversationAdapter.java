package com.flyer.chat.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.google.gson.jpush.JsonElement;
import com.google.gson.jpush.JsonObject;

import java.util.List;

import cn.jpush.im.android.api.model.Message;

/**
 * Created by mike.li on 2018/8/14.
 */

public class ConversationAdapter extends BaseQuickAdapter<Message,BaseViewHolder>{
    public ConversationAdapter(List<Message> data) {
        super(R.layout.item_conversation_view, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Message item) {
        JsonElement jsonElement = item.getContent().toJsonElement();
        JsonObject asJsonObject = jsonElement.getAsJsonObject();
        JsonElement text = asJsonObject.get("text");
        helper.setText(R.id.text,text.toString());
    }
}
