package com.flyer.chat.util;

import com.bumptech.glide.request.RequestOptions;
import com.flyer.chat.R;

/**
 * Created by mike.li on 2018/7/27.
 */

public class GlideOptions {
    public static RequestOptions UserOptions(){
        return new RequestOptions().centerCrop().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher);
    }
}
