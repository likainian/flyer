package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.adapter.ConversationAdapter;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.flyer.chat.widget.KeyboardView;

import java.io.File;


/**
 * Created by mike.li on 2018/8/6.
 */

public class ConversationActivity extends BaseActivity implements View.OnClickListener {
    private FrameLayout mToolbarLeft;
    private TextView mToolbarMiddle;
    private FrameLayout mToolbarRight;
    private RecyclerView mRecyclerView;
    private KeyboardView mKeyboard;
    private String name;
    private ConversationAdapter conversationAdapter;

    public static void startActivity(Context context, String name) {
        if(context==null||name==null)return;
        if(name.equals(SharedPreferencesUtil.getInstance().getMobileNo()))return;
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        name = getIntent().getStringExtra("name");
        initView();
        initAdapter();
        initData();
        initReceiver();
    }
    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    private void initReceiver() {
    }

    private void initData() {
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        conversationAdapter = new ConversationAdapter();
        mRecyclerView.setAdapter(conversationAdapter);
    }

    private void initView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mToolbarRight = findViewById(R.id.toolbar_right);
        mRecyclerView = findViewById(R.id.recycler_view);
        mKeyboard = findViewById(R.id.keyboard);
        mToolbarLeft.setOnClickListener(this);
        mToolbarMiddle.setText(name);
        mToolbarRight.setOnClickListener(this);
        mKeyboard.setContentView(mRecyclerView);
        mKeyboard.setOnSendListener(new KeyboardView.OnSendListener() {
            @Override
            public void sendText(String text) {
            }

            @Override
            public void sendFile(File file) {

            }

            @Override
            public void sendVoice(File file, int duration) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                finish();
                break;
            case R.id.toolbar_right:

                break;
        }
    }
    @Override
    public void onBackPressed() {
        if(mKeyboard.hideKeyboardAndPanel()){
            super.onBackPressed();
        }
    }
}
