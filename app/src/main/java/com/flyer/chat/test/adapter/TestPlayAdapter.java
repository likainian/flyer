package com.flyer.chat.test.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.dueeeke.videocontroller.StandardVideoController;
import com.dueeeke.videoplayer.player.VideoView;
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
        TextView mTitle = helper.getView(R.id.tv_title);
        VideoView player = helper.getView(R.id.player);
        ImageView ivThumb = helper.getView(R.id.iv_thumb);

        Glide.with(context).load(item.getThumb()).into(ivThumb);
        mTitle.setText(item.getTitle());
        StandardVideoController controller = new StandardVideoController(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        controller.setLayoutParams(layoutParams);
        controller.addDefaultControlComponent("标题", false);
        player.setVideoController(controller); //设置控制器
        player.setUrl(item.getUrl());
        player.start();

    }

}
