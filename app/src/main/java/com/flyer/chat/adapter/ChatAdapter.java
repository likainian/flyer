package com.flyer.chat.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.util.ConstantUtil;
import com.flyer.chat.util.GlideOptions;
import com.flyer.chat.util.TimeUtil;

import java.util.List;

import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by mike.li on 2018/8/3.
 */

public class ChatAdapter extends BaseQuickAdapter<Conversation,BaseViewHolder>{
    private Context context;
    public ChatAdapter(Context context,List<Conversation> data) {
        super(R.layout.item_chat_view,data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
        UserInfo userInfo = (UserInfo) item.getTargetInfo();
        ImageView userImg = helper.getView(R.id.user_img);
        Glide.with(context).applyDefaultRequestOptions(GlideOptions.UserOptions()).load(ConstantUtil.getImageUrl(userInfo.getAvatar())).into(userImg);
        helper.setText(R.id.user_name,userInfo.getNickname());
        Message latestMessage = item.getLatestMessage();
        helper.setText(R.id.last_message,latestMessage.getContent().getStringExtra("text"));
        helper.setText(R.id.last_message_time, TimeUtil.longToYMDHM(latestMessage.getCreateTime()));
        helper.setText(R.id.unread_count,String.valueOf(item.getUnReadMsgCnt()));
    }
}
