package com.flyer.chat.bean;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by mike.li on 2018/7/16.
 */

public class MapUser {

    /**
     * _id : 86
     * _location : 121.458144,31.252157
     * _name : 17717512667
     * _address : 02cf0a082dff2000-上海市
     * _createtime : 2018-11-07 18:09:17
     * _updatetime : 2018-11-07 18:09:30
     * _province : 上海市
     * _city : 上海市
     * _district : 静安区
     * _distance : 3
     * _image : []
     */
    @JSONField(name="_id")
    private String id;
    @JSONField(name="_location")
    private String location;
    @JSONField(name="_name")
    private String name;
    @JSONField(name="_address")
    private String address;
    @JSONField(name="_createtime")
    private String createtime;
    @JSONField(name="_updatetime")
    private String updatetime;
    @JSONField(name="_province")
    private String province;
    @JSONField(name="_city")
    private String city;
    @JSONField(name="_district")
    private String district;
    @JSONField(name="_distance")
    private String distance;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public String getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(String updatetime) {
        this.updatetime = updatetime;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "MapUser{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", createtime='" + createtime + '\'' +
                ", updatetime='" + updatetime + '\'' +
                ", province='" + province + '\'' +
                ", city='" + city + '\'' +
                ", district='" + district + '\'' +
                ", distance='" + distance + '\'' +
                '}';
    }
}
