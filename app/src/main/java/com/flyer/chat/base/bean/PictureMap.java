package com.flyer.chat.base.bean;

import android.text.TextUtils;

import java.io.File;
import java.util.List;

/**
 * Created by mike.li on 2018/8/21.
 */

public class PictureMap {
    private String name;
    private List<File> files;

    public PictureMap(String name, List<File> files) {
        this.name = name;
        this.files = files;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<File> getFiles() {
        return files;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof PictureMap){
            PictureMap pictureMap = (PictureMap) obj;
            return TextUtils.equals(this.getName(),pictureMap.getName());
        }else {
            return false;
        }
    }
}
