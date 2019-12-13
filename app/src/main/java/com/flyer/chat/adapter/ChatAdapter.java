package com.flyer.chat.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;

/**
 * Created by mike.li on 2018/8/3.
 */

public class ChatAdapter extends BaseQuickAdapter<UnknownError,BaseViewHolder>{
    private Context context;
    public ChatAdapter(Context context) {
        super(R.layout.item_chat_view);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final UnknownError item) {
    }
}
