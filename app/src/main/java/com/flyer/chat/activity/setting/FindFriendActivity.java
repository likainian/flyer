package com.flyer.chat.activity.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.ConversationActivity;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.util.HttpParseUtil;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.ToastUtil;
import com.mob.jimu.query.Condition;
import com.mob.jimu.query.Query;
import com.mob.jimu.query.data.Text;
import com.mob.tools.utils.Hashon;
import com.mob.ums.QueryView;
import com.mob.ums.UMSSDK;
import com.mob.ums.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by mike.li on 2018/8/28.
 */

public class FindFriendActivity extends BaseActivity implements View.OnClickListener {
    private FrameLayout mToolbarLeft;
    private TextView mToolbarMiddle;
    private TextView mToolbarRight;
    private RelativeLayout mToolbar;
    private EditText mEditName;
    private TextView mBtnSearch;
    private LinearLayout mSearchResult;
    private TextView mResultName;
    private RecyclerView mNearFriend;

    public static void startActivity(Context context) {
        context.startActivity(new Intent(context, FindFriendActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_friend);
        initView();
    }

    private void initView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mToolbarRight = findViewById(R.id.toolbar_right);
        mToolbar = findViewById(R.id.toolbar);
        mEditName = findViewById(R.id.edit_name);
        mBtnSearch = findViewById(R.id.btn_search);
        mSearchResult = findViewById(R.id.search_result);
        mResultName = findViewById(R.id.result_name);
        mSearchResult.setOnClickListener(this);
        mNearFriend = findViewById(R.id.near_friend);
        mToolbarLeft.setOnClickListener(this);
        mToolbarMiddle.setText("查找朋友");
        mToolbarRight.setText("我的");
        mSearchResult.setVisibility(View.GONE);
        mToolbarRight.setOnClickListener(this);
        mBtnSearch.setOnClickListener(this);
        mEditName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                searchFriend(mEditName.getText().toString().trim());
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.toolbar_right:
                UserInfoActivity.startActivity(this);
                break;
            case R.id.btn_search:
                if (CheckUtil.isEmpty(mEditName.getText().toString().trim())) return;
                searchFriend(mEditName.getText().toString().trim());
                break;

            case R.id.search_result:
                ConversationActivity.startActivity(this, mResultName.getText().toString().trim());
                break;
        }
    }

    private void searchFriend(final String userName) {
        Observable.create(new ObservableOnSubscribe<User>() {
            @Override
            public void subscribe(ObservableEmitter<User> emitter) throws Exception {
                try {
                    Query query = UMSSDK.getQuery(QueryView.USERS);
                    Query condition = query.condition(Condition.eq("phone", Text.valueOf(userName)));
                    String result = condition.query();
                    List<String> list = HttpParseUtil.parseArray(result, "list", String.class);
                    ArrayList<User> users = new ArrayList<>();
                    for (String s : list) {
                        HashMap<String, Object> map = new Hashon().fromJson(s);
                        User user = new User();
                        user.parseFromMap(map);
                        users.add(user);
                    }
                    LogUtil.i("ttt", users.toString());
                    if (users.size() > 0) {
                        emitter.onNext(users.get(0));
                    }
                } catch (Throwable throwable) {
                    throw  new Exception(throwable);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<User>() {
                    @Override
                    public void accept(User user) throws Exception {
                        mSearchResult.setVisibility(View.VISIBLE);
                        mResultName.setText(user.phone.get());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                        ToastUtil.showToast("没有查到该用户");
                    }
                });
    }
}
