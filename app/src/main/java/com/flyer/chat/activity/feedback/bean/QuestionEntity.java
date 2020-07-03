package com.flyer.chat.activity.feedback.bean;

import java.util.ArrayList;

import cn.bmob.v3.BmobObject;

/**
 * Created by mike.li on 2019/12/9.
 */
public class QuestionEntity extends BmobObject {
    private String type;
    private String message;
    private String contact;
    private ArrayList<String> images;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }
}
