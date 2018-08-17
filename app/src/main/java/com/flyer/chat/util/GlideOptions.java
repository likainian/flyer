package com.flyer.chat.util;

import com.bumptech.glide.request.RequestOptions;
import com.flyer.chat.R;

/**
 * Created by mike.li on 2018/7/27.
 */

public class GlideOptions {
    public static RequestOptions UserOptions(){
        return new RequestOptions().centerCrop().placeholder(R.drawable.default_head).error(R.drawable.default_head);
    }
    public static RequestOptions ImageOptions(){
        return new RequestOptions().centerCrop().placeholder(R.drawable.default_image).error(R.drawable.default_image);
    }
}
