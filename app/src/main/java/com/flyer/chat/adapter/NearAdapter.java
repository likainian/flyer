package com.flyer.chat.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.activity.ConversationActivity;
import com.flyer.chat.bean.User;
import com.flyer.chat.util.ConstantUtil;
import com.flyer.chat.util.GlideOptions;

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
    protected void convert(BaseViewHolder helper, final User item) {
        ImageView userImg = helper.getView(R.id.user_img);
        Glide.with(context).applyDefaultRequestOptions(GlideOptions.UserOptions()).load(ConstantUtil.getImageUrl(item.getImg())).into(userImg);
        helper.setText(R.id.user_name,item.getName());
        helper.setText(R.id.user_sex,item.getSex());
        helper.setText(R.id.user_age,String.valueOf(item.getAge()));
        helper.setText(R.id.sign,item.getSign());
        helper.setText(R.id.location,item.getLocation());
        helper.getConvertView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //第二个参数是appkey,如果填空则默认为本应用的appkey
                ConversationActivity.startActivity(context,item.getName());
            }
        });
    }
}
