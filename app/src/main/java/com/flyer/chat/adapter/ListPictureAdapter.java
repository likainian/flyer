package com.flyer.chat.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.bean.PictureMap;
import com.flyer.chat.util.GlideOptions;

import java.io.File;

/**
 * Created by mike.li on 2018/8/21.
 */

public class ListPictureAdapter extends BaseQuickAdapter<PictureMap,BaseViewHolder> {
    private Context context;
    public ListPictureAdapter(Context context) {
        super(R.layout.item_list_picture_view);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, PictureMap item) {
        helper.setText(R.id.tv_name,item.getName());
        helper.setText(R.id.tv_num,String.valueOf(item.getFiles().size()));
        ImageView imageView = helper.getView(R.id.iv_image);
        File file = item.getFiles().get(0);
        Glide.with(context).applyDefaultRequestOptions(GlideOptions.ImageOptions()).load(file).into(imageView);
    }
}
