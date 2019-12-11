package com.flyer.chat.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.activity.ConversationActivity;
import com.flyer.chat.bean.MapUser;
import com.flyer.chat.util.GlideOptions;

/**
 * Created by mike.li on 2018/7/27.
 */

public class NearAdapter extends BaseQuickAdapter<MapUser,BaseViewHolder>{
    private Context context;
    public NearAdapter(Context context) {
        super(R.layout.item_user_info);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final MapUser item) {
        ImageView userImg = helper.getView(R.id.user_img);
    }
}
