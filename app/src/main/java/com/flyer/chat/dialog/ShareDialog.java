package com.flyer.chat.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.flyer.chat.R;
import com.flyer.chat.bean.IconItem;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.LogUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.jiguang.share.android.api.JShareInterface;
import cn.jiguang.share.android.api.PlatActionListener;
import cn.jiguang.share.android.api.Platform;
import cn.jiguang.share.android.api.ShareParams;
import cn.jiguang.share.qqmodel.QQ;
import cn.jiguang.share.qqmodel.QZone;
import cn.jiguang.share.wechat.Wechat;
import cn.jiguang.share.wechat.WechatMoments;

/**
 * Created by mike.li on 2018/8/29.
 */

public class ShareDialog extends AlertDialog{
    private Context context;

    public ShareDialog(@NonNull Context context) {
        super(context, R.style.SelectDialog);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_share_layout);
        RecyclerView mSelectList = findViewById(R.id.select_list);
        if (mSelectList != null) {
            mSelectList.setLayoutManager(new GridLayoutManager(context,4));
            GridAdapter adapter = new GridAdapter(getIconItem());
            mSelectList.setAdapter(adapter);
            adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    dismiss();
                    IconItem item = (IconItem) adapter.getItem(position);
                    shareWeb(item.getItemId());
                }
            });
        }
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.gravity = Gravity.BOTTOM;
            layoutParams.windowAnimations = R.style.anim_y_bottom;
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            getWindow().setAttributes(layoutParams);
        }
    }

    private class GridAdapter extends BaseQuickAdapter<IconItem,BaseViewHolder>{
        @Override
        public int getItemCount() {
            if(mData.size()%4!=0){
                for (int i = 0; i <mData.size()%4 ; i++) {
                    mData.add(new IconItem());
                }
            }
            return super.getItemCount();
        }

        private GridAdapter(List<IconItem> data) {
            super(R.layout.item_dialog_share,data);
        }

        @Override
        protected void convert(BaseViewHolder helper, IconItem item) {
            helper.setText(R.id.share_name,item.getName());
            helper.setImageResource(R.id.share_image,item.getImageRes());
        }
    }
    private static List<IconItem> getIconItem(){
        final List<String> platformList = JShareInterface.getPlatformList();
        ArrayList<IconItem> iconItems = new ArrayList<>();
        for (String string:platformList){
            if(QQ.Name.equals(string)){
                iconItems.add(new IconItem(QQ.Name,"qq", R.drawable.share_qq));
            }else if(QZone.Name.equals(string)){
                iconItems.add(new IconItem(QZone.Name,"空间", R.drawable.share_qzone));
            }else if(Wechat.Name.equals(string)){
                iconItems.add(new IconItem(Wechat.Name,"微信", R.drawable.share_wechat));
            }else if(WechatMoments.Name.equals(string)){
                iconItems.add(new IconItem(WechatMoments.Name,"朋友圈", R.drawable.share_wechat_moments));
            }
        }
        return iconItems;
    }
    private static void shareWeb(String name){
        ShareParams shareParams = new ShareParams();
        shareParams.setShareType(Platform.SHARE_WEBPAGE);
        shareParams.setTitle("标题");
        shareParams.setText("文本");
        shareParams.setUrl("https://www.baidu.com");
        shareParams.setImagePath(DeviceUtil.getSavePicturePath("avatar"));
        JShareInterface.share(name, shareParams, new PlatActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                LogUtil.i("ttt","分享成功");
            }

            @Override
            public void onError(Platform platform, int i, int i1, Throwable throwable) {
                throwable.printStackTrace();
                LogUtil.i("ttt","分享错误"+i+"  "+i1);
            }

            @Override
            public void onCancel(Platform platform, int i) {
                LogUtil.i("ttt","分享取消");
            }
        });
    }
}
