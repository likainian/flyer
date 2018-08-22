package com.flyer.chat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.util.GlideOptions;
import com.flyer.chat.util.ToastHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by mike.li on 2018/8/21.
 */

public class GridPictureAdapter extends BaseQuickAdapter<File,BaseViewHolder>{
    private Context context;
    private int maxCount;
    private ArrayList<File> select = new ArrayList<>();
    public OnPictureClickListener onPictureClickListener;
    public interface OnPictureClickListener{
        void onPictureClick(int selectCount);
    }

    public void setOnPictureClickListener(OnPictureClickListener onPictureClickListener) {
        this.onPictureClickListener = onPictureClickListener;
    }

    public ArrayList<String> getSelect() {
        ArrayList<String> list = new ArrayList<>();
        for(File file:select){
            list.add(file.getPath());
        }
        return list;
    }

    public GridPictureAdapter(Context context, int maxCount) {
        super(R.layout.item_grid_picture_view);
        this.context = context;
        this.maxCount = maxCount;
    }

    @Override
    protected void convert(BaseViewHolder helper, final File item) {
        ImageView image = helper.getView(R.id.image);
        final CheckBox imageSelect = helper.getView(R.id.image_select);
        Glide.with(context).applyDefaultRequestOptions(GlideOptions.ImageOptions()).load(item).into(image);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(select.contains(item)){
                    select.remove(item);
                    imageSelect.setChecked(false);
                }else {
                    if(select.size()>=maxCount){
                        ToastHelper.showToast("最多只能选"+maxCount+"张图片");
                    }else {
                        select.add(item);
                        imageSelect.setChecked(true);
                    }
                }
                if(onPictureClickListener!=null){
                    onPictureClickListener.onPictureClick(select.size());
                }
            }
        });
    }

    @Override
    public void setNewData(List<File> data) {
        Collections.sort(data, new Comparator<File>() {
            @Override
            public int compare(File lhs, File rhs) {
                return (int) Math.signum((double) rhs.lastModified() - lhs.lastModified());
            }
        });
        super.setNewData(data);
    }
}
