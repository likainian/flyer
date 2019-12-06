package com.flyer.chat.activity.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.bean.Province;
import com.flyer.chat.dialog.SelectDialog;
import com.flyer.chat.util.AssetsUtil;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.TimeUtil;
import com.flyer.chat.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.callback.CreateGroupCallback;
import cn.jpush.im.android.api.callback.GetAvatarBitmapCallback;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by mike.li on 2018/8/10.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener {
    public static final String ACTION_USER = "com.flyer.chat.user";
    public static final int EDIT_NICK_NAME = 12;
    public static final int EDIT_SIGN = 13;
    private ImageView mToolbarLeft;
    private TextView mToolbarMiddle;
    private LinearLayout mHeadLayout;
    private ImageView mHead;
    private LinearLayout mNameLayout;
    private TextView mNickName;
    private LinearLayout mNickNameLayout;
    private TextView mName;
    private LinearLayout mAgeLayout;
    private TextView mGender;
    private LinearLayout mGenderLayout;
    private TextView mAge;
    private LinearLayout mCodeLayout;
    private LinearLayout mLocationLayout;
    private TextView mLocation;
    private LinearLayout mSignLayout;
    private TextView mSign;
    private BroadcastReceiver avatarBroadcastReceiver;
    private UserInfo myInfo;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
        initInfo();
        avatarBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                initInfo();
            }
        };
        registerReceiver(avatarBroadcastReceiver,new IntentFilter(ACTION_USER));
    }

    private void initView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarLeft.setOnClickListener(this);
        mNameLayout = findViewById(R.id.name_layout);
        mNameLayout.setOnClickListener(this);
        mHeadLayout = findViewById(R.id.head_layout);
        mHeadLayout.setOnClickListener(this);
        mNickNameLayout = findViewById(R.id.nick_name_layout);
        mNickNameLayout.setOnClickListener(this);
        mGenderLayout = findViewById(R.id.gender_layout);
        mGenderLayout.setOnClickListener(this);
        mAgeLayout = findViewById(R.id.age_layout);
        mAgeLayout.setOnClickListener(this);
        mLocationLayout = findViewById(R.id.location_layout);
        mLocationLayout.setOnClickListener(this);
        mSignLayout = findViewById(R.id.sign_layout);
        mSignLayout.setOnClickListener(this);
        mCodeLayout = findViewById(R.id.code_layout);
        mCodeLayout.setOnClickListener(this);

        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mName = findViewById(R.id.name);
        mHead = findViewById(R.id.head);
        mNickName = findViewById(R.id.nick_name);
        mAge = findViewById(R.id.age);
        mGender = findViewById(R.id.gender);
        mLocation = findViewById(R.id.location);
        mSign = findViewById(R.id.sign);

    }

    private void initInfo() {
        mToolbarMiddle.setText("个人信息");
        myInfo = JMessageClient.getMyInfo();
        if(myInfo==null)return;
        mName.setText(myInfo.getUserName());
        setHead();
        mNickName.setText(myInfo.getNickname());
        mAge.setText(String.valueOf(TimeUtil.longToAge(myInfo.getBirthday())));
        mGender.setText(myInfo.getGender().name());
        mLocation.setText(myInfo.getAddress());
        mSign.setText(myInfo.getSignature());
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(avatarBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.name_layout:
                ToastUtil.showToast("手机号不能更改");
                break;
            case R.id.head_layout:
            UserHeadActivity.startActivity(this);
            break;
            case R.id.nick_name_layout:
                UserInfoEditActivity.startActivityForResult(this,"设置名字",myInfo.getNickname(),EDIT_NICK_NAME);
                break;
            case R.id.gender_layout:
                ArrayList<String> list = new ArrayList<>();
                list.add("男");
                list.add("女");
                new SelectDialog(this).setList(list).setOnSelectListener(new SelectDialog.OnSelectListener() {
                    @Override
                    public void OnSelect(int position) {
                        switch (position){
                            case 0:
                                myInfo.setGender(UserInfo.Gender.male);
                                JMessageClient.updateMyInfo(UserInfo.Field.gender, myInfo, new CreateGroupCallback() {
                                    @Override
                                    public void gotResult(int i, String s, long l) {
                                        if(i==0){
                                            mGender.setText(myInfo.getGender().name());
                                        }else {
                                            ToastUtil.showToast("修改失败");
                                        }
                                    }
                                });
                                break;
                            case 1:
                                myInfo.setGender(UserInfo.Gender.female);
                                JMessageClient.updateMyInfo(UserInfo.Field.gender, myInfo, new CreateGroupCallback() {
                                    @Override
                                    public void gotResult(int i, String s, long l) {
                                        if(i==0){
                                            mGender.setText(myInfo.getGender().name());
                                        }else {
                                            ToastUtil.showToast("修改失败");
                                        }
                                    }
                                });
                                break;
                        }
                    }
                }).show();
                break;

            case R.id.age_layout:
                Calendar end = Calendar.getInstance();
                end.setTimeInMillis(System.currentTimeMillis());
                Calendar start = Calendar.getInstance();
                start.setTimeInMillis(Long.MIN_VALUE);
                Calendar instance = Calendar.getInstance();
                instance.setTimeInMillis(myInfo.getBirthday());
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(Date date, View v) {
                        myInfo.setBirthday(date.getTime());
                        JMessageClient.updateMyInfo(UserInfo.Field.birthday, myInfo, new CreateGroupCallback() {
                            @Override
                            public void gotResult(int i, String s, long l) {
                                if(i==0){
                                    mAge.setText(String.valueOf(TimeUtil.longToAge(myInfo.getBirthday())));
                                }else {
                                    ToastUtil.showToast("修改失败");
                                }
                            }
                        });
                    }
                }).setDate(instance).setRangDate(null,end).build().show();
                break;
            case R.id.code_layout:
                CodeActivity.startActivity(this);
                break;
            case R.id.location_layout:
                String json = AssetsUtil.getJson(this, "province.json");
                List<Province> provinces = HttpParseUtil.parseArray(json, Province.class);
                final ArrayList<String> options1Items = new ArrayList<>();
                final ArrayList<List<String>> options2Items = new ArrayList<>();
                final ArrayList<List<List<String>>> options3Items = new ArrayList<>();
                for (Province province:provinces){
                    List<Province.City> citys = province.getCity();
                    List<List<String>> areasName = new ArrayList<>();
                    ArrayList<String> cityName = new ArrayList<>();
                    for (Province.City city:citys){
                        cityName.add(city.getName());
                        List<String> areas = city.getArea();
                        areasName.add(areas);
                    }
                    options1Items.add(province.getName());
                    options2Items.add(cityName);
                    options3Items.add(areasName);

                }
                OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3 ,View v) {
                        //返回的分别是三个级别的选中位置
                        String p = options1Items.get(options1);
                        String c = options2Items.get(options1).get(option2);
                        String a = options3Items.get(options1).get(option2).get(options3);
                        if(TextUtils.equals(p,c)){
                            myInfo.setAddress(c+a);
                        }else {
                            myInfo.setAddress(p+c+a);
                        }
                        JMessageClient.updateMyInfo(UserInfo.Field.address, myInfo, new CreateGroupCallback() {
                            @Override
                            public void gotResult(int i, String s, long l) {
                                if(i==0){
                                    mLocation.setText(myInfo.getAddress());
                                }else {
                                    ToastUtil.showToast("修改失败");
                                }
                            }
                        });
                    }
                }).build();
                pvOptions.setPicker(options1Items, options2Items, options3Items);
                pvOptions.show();
                break;
            case R.id.sign_layout:
                UserInfoEditActivity.startActivityForResult(this,"设置签名",myInfo.getSignature(),EDIT_SIGN);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            switch (requestCode){
                case EDIT_NICK_NAME:
                    myInfo.setNickname(data.getStringExtra("name"));
                    JMessageClient.updateMyInfo(UserInfo.Field.nickname, myInfo, new CreateGroupCallback() {
                        @Override
                        public void gotResult(int i, String s, long l) {
                            if(i==0){
                                mNickName.setText(myInfo.getNickname());
                            }else {
                                ToastUtil.showToast("修改失败");
                            }
                        }
                    });
                    break;
                case EDIT_SIGN:
                    myInfo.setSignature(data.getStringExtra("name"));
                    JMessageClient.updateMyInfo(UserInfo.Field.signature, myInfo, new CreateGroupCallback() {
                        @Override
                        public void gotResult(int i, String s, long l) {
                            if(i==0){
                                mSign.setText(myInfo.getSignature());
                            }else {
                                ToastUtil.showToast("修改失败");
                            }
                        }
                    });
                    break;
            }

        }
    }

    private void setHead(){
        myInfo.getAvatarBitmap(new GetAvatarBitmapCallback() {
            @Override
            public void gotResult(int i, String s, Bitmap bitmap) {
                LogUtil.i(i+s);
                if(i==0){
                    mHead.setImageBitmap(bitmap);
                }
            }
        });
    }
}
