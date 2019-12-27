package com.flyer.chat.activity.home.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.util.BitmapUtil;

/**
 * Created by mike.li on 2018/7/27.
 */

public class NearAdapter extends BaseQuickAdapter<User,BaseViewHolder>{
    private Context context;
    public NearAdapter(Context context) {
        super(R.layout.item_user_info);
        this.context = context;
    }

    @Override
    protected void convert(final BaseViewHolder helper, final User item) {
        ImageView userImg = helper.getView(R.id.iv_head);
        Bitmap bitmap = BitmapUtil.base64ToBitmap(item.getImg());
        if(bitmap!=null){
            userImg.setImageBitmap(bitmap);
        }
        helper.setText(R.id.tv_name,item.getNikeName());
        helper.setText(R.id.tv_sex,item.getSex());
        helper.setText(R.id.tv_age,String.valueOf(item.getAge()));
        helper.setText(R.id.tv_sign,item.getSign());
        helper.setText(R.id.tv_location,item.getLocation());
    }
}
