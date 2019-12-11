package com.flyer.chat.activity.setting;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.flyer.chat.R;
import com.flyer.chat.activity.common.CodeActivity;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.bean.Province;
import com.flyer.chat.dialog.SelectDialog;
import com.flyer.chat.network.UMSCallback;
import com.flyer.chat.util.AssetsUtil;
import com.flyer.chat.util.GlideOptions;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.TimeUtil;
import com.flyer.chat.util.ToastUtil;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;
import com.mob.ums.datatype.Gender;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by mike.li on 2018/8/10.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener, UserInfoContract.UserInfoView {
    public static final String ACTION_USER = "com.flyer.chat.user";
    public static final int EDIT_NICK_NAME = 12;
    public static final int EDIT_SIGN = 13;
    private ImageView mHead;
    private TextView mNickName;
    private TextView mName;
    private TextView mGender;
    private TextView mAge;
    private TextView mLocation;
    private TextView mSign;

    private BroadcastReceiver avatarBroadcastReceiver;
    private User myInfo;
    private UserInfoPresenter mPresenter;


    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        mPresenter = new UserInfoPresenter(this);
        initView();
        mPresenter.getUserInfo();
        avatarBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mPresenter.getUserInfo();
            }
        };
        registerReceiver(avatarBroadcastReceiver,new IntentFilter(ACTION_USER));
    }

    private void initView() {
        FrameLayout mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarLeft.setOnClickListener(this);
        LinearLayout mHeadLayout = findViewById(R.id.head_layout);
        mHeadLayout.setOnClickListener(this);
        LinearLayout mNickNameLayout = findViewById(R.id.nick_name_layout);
        mNickNameLayout.setOnClickListener(this);
        LinearLayout mGenderLayout = findViewById(R.id.gender_layout);
        mGenderLayout.setOnClickListener(this);
        LinearLayout mAgeLayout = findViewById(R.id.age_layout);
        mAgeLayout.setOnClickListener(this);
        LinearLayout mLocationLayout = findViewById(R.id.location_layout);
        mLocationLayout.setOnClickListener(this);
        LinearLayout mSignLayout = findViewById(R.id.sign_layout);
        mSignLayout.setOnClickListener(this);
        LinearLayout mCodeLayout = findViewById(R.id.code_layout);
        mCodeLayout.setOnClickListener(this);

        mName = findViewById(R.id.name);
        mHead = findViewById(R.id.head);
        mNickName = findViewById(R.id.nick_name);
        mAge = findViewById(R.id.age);
        mGender = findViewById(R.id.gender);
        mLocation = findViewById(R.id.location);
        mSign = findViewById(R.id.sign);

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
            case R.id.head_layout:
                UserHeadActivity.startActivity(this);
                break;
            case R.id.nick_name_layout:
                UserInfoEditActivity.startActivityForResult(this,"设置名字",myInfo.nickname.get(),EDIT_NICK_NAME);
                break;
            case R.id.gender_layout:
                final ArrayList<String> list = new ArrayList<>();
                list.add("男");
                list.add("女");
                new SelectDialog(this).setList(list).setOnSelectListener(new SelectDialog.OnSelectListener() {
                    @Override
                    public void OnSelect(final int position) {
                        HashMap<String, Object> map = new HashMap<>();
                        Gender gender;
                        if(position==0){
                            gender = Gender.Male.INSTANCE;
                        }else {
                            gender = Gender.Female.INSTANCE;
                        }
                        map.put("gender",gender);
                        UMSSDK.updateUserInfo(map,new UMSCallback<Void>(){
                            @Override
                            public void onSuccess(Void aVoid) {
                                super.onSuccess(aVoid);
                                mGender.setText(list.get(position));
                            }
                        });
                    }
                }).show();
                break;

            case R.id.age_layout:
                Calendar end = Calendar.getInstance();
                end.setTimeInMillis(System.currentTimeMillis());
                Calendar start = Calendar.getInstance();
                start.setTimeInMillis(Long.MIN_VALUE);
                Calendar instance = Calendar.getInstance();
                if(myInfo.birthday.get()!=null){
                    instance.setTimeInMillis(myInfo.birthday.get().getTime());
                }else {
                    instance.setTimeInMillis(System.currentTimeMillis());
                }
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(final Date date, View v) {
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("birthday",date);
                        UMSSDK.updateUserInfo(map,new UMSCallback<Void>(){
                            @Override
                            public void onSuccess(Void aVoid) {
                                super.onSuccess(aVoid);
                                mAge.setText(String.valueOf(TimeUtil.longToAge(date.getTime())));
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
                        final String address;
                        if(TextUtils.equals(p,c)){
                            address = c+a;
                        }else {
                            address = p+c+a;
                        }
                        HashMap<String, Object> map = new HashMap<>();
                        String oldAddress = myInfo.address.get();
                        if(oldAddress.contains("-")){
                            oldAddress = oldAddress.substring(0,oldAddress.indexOf("-")+1)+address;
                        }
                        map.put("addr",oldAddress);
                        UMSSDK.updateUserInfo(map,new UMSCallback<Void>(){
                            @Override
                            public void onSuccess(Void aVoid) {
                                super.onSuccess(aVoid);
                                mLocation.setText(address);
                            }
                        });
                    }
                }).build();
                pvOptions.setPicker(options1Items, options2Items, options3Items);
                pvOptions.show();
                break;
            case R.id.sign_layout:
                UserInfoEditActivity.startActivityForResult(this,"设置签名",myInfo.signature.get(),EDIT_SIGN);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            HashMap<String, Object> map = new HashMap<>();
            switch (requestCode){
                case EDIT_NICK_NAME:
                    final String nickName = data.getStringExtra("name");
                    map.put("nickname",nickName);
                    UMSSDK.updateUserInfo(map,new UMSCallback<Void>(){
                        @Override
                        public void onSuccess(Void aVoid) {
                            super.onSuccess(aVoid);
                            mNickName.setText(nickName);
                        }
                    });
                    break;
                case EDIT_SIGN:
                    final String signature = data.getStringExtra("name");
                    map.put("signature",signature);
                    UMSSDK.updateUserInfo(map,new UMSCallback<Void>(){
                        @Override
                        public void onSuccess(Void aVoid) {
                            super.onSuccess(aVoid);
                            mSign.setText(signature);
                        }
                    });
                    break;
            }

        }
    }

    @Override
    public void showUserInfo(User user) {
        myInfo = user;
        mName.setText(myInfo.phone.get());
        String[] strings = myInfo.avatar.get();
        if(strings==null){
            mHead.setImageResource(R.drawable.default_head);
        }else {
            Glide.with(UserInfoActivity.this).applyDefaultRequestOptions(GlideOptions.UserOptions()).load(myInfo.avatar.get()[0]).into(mHead);
        }
        mNickName.setText(myInfo.nickname.get());
        if(myInfo.gender.get()!=null){
            mGender.setText(myInfo.gender.get()==Gender.Male.INSTANCE?"男":"女");
        }else {
            mGender.setText("未知");
        }
        int age = TimeUtil.longToAge(myInfo.birthday.get()!=null?myInfo.birthday.get().getTime():System.currentTimeMillis());
        mAge.setText(String.valueOf(age));
        String address = myInfo.address.get();
        mLocation.setText(address);
        mSign.setText(myInfo.signature.get());
    }
}
