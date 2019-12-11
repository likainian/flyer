package com.flyer.chat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.activity.ConversationActivity;
import com.flyer.chat.util.GlideOptions;
import com.flyer.chat.util.TimeUtil;
import com.mob.imsdk.model.IMConversation;

/**
 * Created by mike.li on 2018/8/3.
 */

public class ChatAdapter extends BaseQuickAdapter<IMConversation,BaseViewHolder>{
    private Context context;
    public ChatAdapter(Context context) {
        super(R.layout.item_chat_view);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final IMConversation item) {
        ImageView userImg = helper.getView(R.id.user_img);
        String avatar = item.getOtherInfo().getAvatar();
        Glide.with(context).applyDefaultRequestOptions(GlideOptions.UserOptions()).load(avatar).into(userImg);
        helper.setText(R.id.user_name,item.getOtherInfo().getNickname());
        helper.setText(R.id.last_message,item.getLastMessage().getBody());
        helper.setText(R.id.last_message_time, TimeUtil.longToYMDHM(item.getLastMessage().getCreateTime()));
        helper.setText(R.id.unread_count,String.valueOf(item.getUnreadMsgCount()));
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConversationActivity.startActivity(context,item.getOtherInfo().getId());
            }
        });
    }
}
