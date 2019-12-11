package com.flyer.chat.activity.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.HomeActivity;
import com.flyer.chat.app.ChatApplication;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.bean.LinkUser;
import com.flyer.chat.dialog.TitleSelectDialog;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.map.ImCloud;
import com.flyer.chat.map.MapCloud;
import com.flyer.chat.map.NewCloud;
import com.flyer.chat.network.CallBack;
import com.flyer.chat.network.UMSCallback;
import com.flyer.chat.push.PushReceiver;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.DeviceUtil;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.KeyBoardUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.mob.MobSDK;
import com.mob.pushsdk.MobPush;
import com.mob.ums.User;

import java.util.List;

/**
 * Created by mike.li on 2018/7/9.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, LoginContract.LoginView {

    public static void startActivity(Context context){
        context.startActivity(new Intent(context,LoginActivity.class));
    }

    private ScrollView mScrollView;
    private EditText mMobileNo;
    private EditText mPassword;
    private ImageView mSeePassword;
    private ImageView mSavePassword;
    private TextView mBtnLogin;

    private LoginPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ChatApplication.getInstance().clearAllActivities();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mPresenter = new LoginPresenter(this);
        initView();
    }

    private void initView() {
        mScrollView = findViewById(R.id.scroll_view);
        mMobileNo = findViewById(R.id.mobile_no);
        mPassword = findViewById(R.id.password);
        mSeePassword = findViewById(R.id.see_password);
        LinearLayout mLlSavePassword = findViewById(R.id.ll_save_password);
        mSavePassword = findViewById(R.id.save_password);
        TextView mGoRegister = findViewById(R.id.go_register);

        mBtnLogin.setOnClickListener(this);
        mGoRegister.setOnClickListener(this);
        mLlSavePassword.setOnClickListener(this);
        mSeePassword.setOnClickListener(this);
        mMobileNo.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtnLogin.setEnabled(loginEnable());
            }
        });
        mPassword.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mBtnLogin.setEnabled(loginEnable());
            }
        });
        KeyBoardUtil.register(this, new KeyBoardUtil.KeyBoardStatusListener() {
            @Override
            public void onKeyBoardStateChanged(boolean isShow, int heightDiff) {
                int[] location = new int[2];
                mBtnLogin.getLocationOnScreen(location);
                final int scrollY = location[1]+mBtnLogin.getHeight()-(DeviceUtil.getDisplayHeight(LoginActivity.this)-heightDiff);
                if (scrollY > 0) {
                    mScrollView.smoothScrollBy(0, scrollY+DeviceUtil.dip2px(LoginActivity.this,10));
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.go_register:
                RegisterActivity.startActivity(this);
                break;
            case R.id.see_password:
                switchShowPassword();
                break;
            case R.id.user_more:
                final List<String> userNameList = SharedPreferencesUtil.getInstance().getMobileNoList();
                new TitleSelectDialog(this)
                        .setTitle("选择手机号")
                        .setList(userNameList)
                        .setCheck(mMobileNo.getText().toString().trim())
                        .setOnSelectListener(new TitleSelectDialog.OnSelectListener() {
                            @Override
                            public void OnSelect(int position) {
                                mMobileNo.setText(userNameList.get(position));
                            }
                        }).show();
                break;
        }
    }

    private void switchShowPassword() {
        if (mPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            mSeePassword.setImageResource(R.drawable.password_open_eye);
            mPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            mSeePassword.setImageResource(R.drawable.password_colse_eye);
            mPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
    }

    private boolean loginEnable(){
        return CheckUtil.isNotEmpty(mMobileNo.getText().toString().trim())
                &&CheckUtil.isNotEmpty(mPassword.getText().toString().trim());
    }

    @Override
    public void loginSuccess(User user) {
        LogUtil.i("ttt","登陆成功："+user);
        KeyBoardUtil.hideKeyBoard(mBtnLogin);
        LinkUser linkUser = HttpParseUtil.parseObject(user.signature.get(), LinkUser.class);
        if(linkUser == null||CheckUtil.isEmpty(linkUser.getMapId())||CheckUtil.isEmpty(linkUser.getToken())){
            MapCloud.CreateMapUser(user, new CallBack<String>() {
                @Override
                public void onResponse(String response) {
                    ImCloud.setMapId(response,new UMSCallback<Void>());
                }
            });
            NewCloud.getToken(new CallBack<String>() {
                @Override
                public void onResponse(String response) {
                    ImCloud.setToken(response,new UMSCallback<Void>());
                }
            });

        }else {
        }
        MobSDK.setUser(user.id.get(), user.nickname.get(),user.avatar.get()[0], null);
        MobPush.addPushReceiver(PushReceiver.getInstance());
        HomeActivity.startActivity(LoginActivity.this);
    }
}
