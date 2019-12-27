package com.flyer.chat.activity.setting;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.flyer.chat.activity.account.bean.User;
import com.flyer.chat.activity.common.CodeActivity;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.activity.setting.bean.Province;
import com.flyer.chat.dialog.SelectDialog;
import com.flyer.chat.util.AssetsUtil;
import com.flyer.chat.util.BitmapUtil;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.ToastUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by mike.li on 2019/12/16.
 */
public class UserInfoActivity extends ToolbarActivity implements View.OnClickListener {

    private static final int EDIT_NICK_NAME = 23;
    private static final int EDIT_SIGN = 24;
    private TextView mTvNickName;
    private TextView mTvSex;
    private TextView mTvAge;
    private TextView mTvLocation;
    private TextView mTvSign;
    private TextView mTvAccount;
    private ImageView mIvHead;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, UserInfoActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        User user = BmobUser.getCurrentUser(User.class);
        mTvAccount.setText(user.getMobilePhoneNumber());
        mTvNickName.setText(user.getNikeName());
        mTvSex.setText(user.getSex());
        mTvAge.setText(String.valueOf(user.getAge()));
        mTvLocation.setText(user.getLocation());
        mTvSign.setText(user.getSign());
        showHead();
    }

    private void showHead(){
        User user = BmobUser.getCurrentUser(User.class);
        Bitmap bitmap = BitmapUtil.base64ToBitmap(user.getImg());
        if(bitmap!=null){
            mIvHead.setImageBitmap(bitmap);
        }
    }

    private void initView() {
        LinearLayout mLlNickName = findViewById(R.id.ll_nick_name);
        mLlNickName.setOnClickListener(this);
        LinearLayout mLlHead = findViewById(R.id.ll_head);
        mLlHead.setOnClickListener(this);
        LinearLayout mLlSex = findViewById(R.id.ll_sex);
        mLlSex.setOnClickListener(this);
        LinearLayout mLlAge = findViewById(R.id.ll_age);
        mLlAge.setOnClickListener(this);
        LinearLayout mLlLocation = findViewById(R.id.ll_location);
        mLlLocation.setOnClickListener(this);
        LinearLayout mLlSign = findViewById(R.id.ll_sign);
        mLlSign.setOnClickListener(this);
        LinearLayout mLlCode = findViewById(R.id.ll_code);
        mLlCode.setOnClickListener(this);

        mTvAccount = findViewById(R.id.tv_account);
        mTvNickName = findViewById(R.id.tv_nick_name);
        mIvHead = findViewById(R.id.iv_head);
        mTvSex = findViewById(R.id.tv_sex);
        mTvAge = findViewById(R.id.tv_age);
        mTvLocation = findViewById(R.id.tv_location);
        mTvSign = findViewById(R.id.tv_sign);

        setToolbarMiddleText("个人信息");

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_nick_name:
                UserInfoEditActivity.startActivityForResult(this,"设置名字","",EDIT_NICK_NAME);
                break;
            case R.id.ll_head:
                UserHeadActivity.startActivity(this);
                break;
            case R.id.ll_sex:
                final ArrayList<String> list = new ArrayList<>();
                list.add("男");
                list.add("女");
                new SelectDialog(this).setList(list).setOnSelectListener(new SelectDialog.OnSelectListener() {
                    @Override
                    public void OnSelect(final int position) {
                        User user = BmobUser.getCurrentUser(User.class);
                        user.setSex(list.get(position));
                        upLoadUser(user);
                    }
                }).show();
                break;

            case R.id.ll_age:
                final Calendar instance = Calendar.getInstance();
                new TimePickerBuilder(this, new OnTimeSelectListener() {
                    @Override
                    public void onTimeSelect(final Date date, View v) {
                        User user = BmobUser.getCurrentUser(User.class);
                        instance.setTimeInMillis(date.getTime());
                        user.setYear(instance.get(Calendar.YEAR));
                        user.setMonth(instance.get(Calendar.MONTH));
                        user.setDay(instance.get(Calendar.DAY_OF_MONTH));
                        upLoadUser(user);
                    }
                }).setDate(instance).build().show();
                break;
            case R.id.ll_code:
                CodeActivity.startActivity(this);
                break;
            case R.id.ll_location:
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
                        User user = BmobUser.getCurrentUser(User.class);
                        user.setLocation(address);
                        upLoadUser(user);
                    }
                }).build();
                pvOptions.setPicker(options1Items, options2Items, options3Items);
                pvOptions.show();
                break;
            case R.id.ll_sign:
                UserInfoEditActivity.startActivityForResult(this,"设置签名","",EDIT_SIGN);
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            User user = BmobUser.getCurrentUser(User.class);
            switch (requestCode){
                case EDIT_NICK_NAME:
                    String nickName = data.getStringExtra("name");
                    user.setNikeName(nickName);
                    upLoadUser(user);
                    break;
                case EDIT_SIGN:
                    String signature = data.getStringExtra("name");
                    user.setSign(signature);
                    upLoadUser(user);
                    break;
            }

        }
    }

    private void upLoadUser(User user){
        showLoadingDialog();
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                closeLoadingDialog();
                if (e == null) {
                    initData();
                    ToastUtil.showToast("更新用户信息成功");
                } else {
                    ToastUtil.showToast("更新用户信息失败：" + e.getMessage());
                }
            }
        });
    }

}
