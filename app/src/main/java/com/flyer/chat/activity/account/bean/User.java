package com.flyer.chat.activity.account.bean;

import java.util.Calendar;

import cn.bmob.v3.BmobUser;

/**
 * Created by mike.li on 2018/7/16.
 */

public class User extends BmobUser {
    private String img;
    private String nikeName;
    private Integer year = 0;
    private Integer month = 0;
    private Integer day = 0;
    private String sex;
    private Double latitude;
    private Double longitude;
    private String location;
    private String sign;

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getNikeName() {
        return nikeName;
    }

    public void setNikeName(String nikeName) {
        this.nikeName = nikeName;
    }

    public int getAge() {
        Calendar birth = Calendar.getInstance();
        birth.set(year,month,day);
        Calendar cal = Calendar.getInstance();
        if (cal.before(birth)) { //出生日期晚于当前时间，无法计算
            return 0;
        }
        int yearNow = cal.get(Calendar.YEAR);  //当前年份
        int monthNow = cal.get(Calendar.MONTH);  //当前月份
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH); //当前日期
        int yearBirth = birth.get(Calendar.YEAR);
        int monthBirth = birth.get(Calendar.MONTH);
        int dayOfMonthBirth = birth.get(Calendar.DAY_OF_MONTH);
        int age = yearNow - yearBirth;   //计算整岁数
        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) age--;//当前日期在生日之前，年龄减一
            } else {
                age--;//当前月份在生日之前，年龄减一
            }
        }
        return age;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "User{" +
                "mobileNum='" + getMobilePhoneNumber() + '\'' +
                "img='" + img + '\'' +
                ", nikeName='" + nikeName + '\'' +
                ", year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", sex='" + sex + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location='" + location + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }
}
