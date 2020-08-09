package com.flyer.chat.test.adapter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.test.bean.VideoBean;

/**
 * Created by mike.li on 2020/7/8.
 */
public class TestPlayAdapter extends BaseQuickAdapter<VideoBean, BaseViewHolder> {
    private Context context;
    public TestPlayAdapter(Context context) {
        super(R.layout.item_test_play);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, VideoBean item) {

    }

}
