package com.flyer.chat.bean;

import android.support.annotation.DrawableRes;

/**
 * Created by mike.li on 2018/8/30.
 */

public class IconItem {
    private String itemId;
    private String name;
    @DrawableRes
    private int imageRes;

    public IconItem() {
    }

    public IconItem(String itemId, String name, int imageRes) {
        this.itemId = itemId;
        this.name = name;
        this.imageRes = imageRes;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(@DrawableRes int imageRes) {
        this.imageRes = imageRes;
    }
}
