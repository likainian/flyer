package com.flyer.sharesdk;

import android.content.Context;

import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by mike.li on 2018/9/10.
 */

public class ShareSDK {
    public static void showShare(Context context,ShareEntity share){
        if (null == share) {
            return;
        }
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
//        oks.disableSSOWhenAuthorize();
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(share.getShareTitle());
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl(share.getShareUrl());
        // text是分享文本，所有平台都需要这个字段
        oks.setText(share.getShareContent());
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImageUrl(share.getShareImgUrl());
        oks.setImagePath(share.getShareImgPath());
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(share.getShareUrl());
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("https://www.ihr360.com");
        oks.show(context);
    }
}
