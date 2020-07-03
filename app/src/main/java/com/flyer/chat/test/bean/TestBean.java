package com.flyer.chat.test.bean;

/**
 * Created by mike.li on 2020/6/18.
 */
public class TestBean {
    private String name;
    private int resourceId;

    public TestBean(String name, int resourceId) {
        this.name = name;
        this.resourceId = resourceId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
