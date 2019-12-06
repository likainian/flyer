package com.flyer.chat.activity.feedback;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.activity.BigPictureActivity;

import java.util.List;

/**
 * Created by mike.li on 2019/11/6.
 */
public class GridPhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public static final String DEFAULT = "DEFAULT";
    private Context context;
    private int maxCount;
    private OnClickAddListener onClickAddListener;
    public interface OnClickAddListener{
        void OnClickAdd();
    }

    public GridPhotoAdapter(Context context,int maxCount) {
        super(R.layout.item_grid_photo_view);
        this.context = context;
        this.maxCount = maxCount;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final String item) {
        ImageView ivImage = helper.getView(R.id.iv_image);
        if(DEFAULT.equals(item)){
            Glide.with(context).load(R.drawable.picture_add).into(ivImage);
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onClickAddListener!=null)onClickAddListener.OnClickAdd();
                }
            });
        }else {
            Glide.with(context).load(item).into(ivImage);
            ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> paths = getData();
                    paths.remove(DEFAULT);
                    BigPictureActivity.startActivity(context,paths,helper.getAdapterPosition());
                }
            });
        }

    }

    @Override
    public void setNewData(List<String> data) {
        data.remove(DEFAULT);
        if(data.size()<maxCount){
            data.add(DEFAULT);
        }
        super.setNewData(data);
    }

    public void setOnClickAddListener(OnClickAddListener onClickAddListener) {
        this.onClickAddListener = onClickAddListener;
    }
}
