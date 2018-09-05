package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.adapter.ConversationAdapter;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.bean.MessageItem;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.SharedPreferencesHelper;
import com.flyer.chat.util.ToastHelper;
import com.flyer.chat.widget.KeyboardView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.api.BasicCallback;

/**
 * Created by mike.li on 2018/8/6.
 */

public class ConversationActivity extends BaseActivity implements View.OnClickListener {
    private Conversation conversation;
    private ImageView mToolbarLeft;
    private TextView mToolbarMiddle;
    private TextView mToolbarRight;
    private RelativeLayout mToolbar;
    private RecyclerView mRecyclerView;
    private KeyboardView mKeyboard;
    private String name;
    private ConversationAdapter conversationAdapter;

    public static void startActivity(Context context, String name) {
        if(context==null||name==null)return;
        if(name.equals(SharedPreferencesHelper.getInstance().getUserName()))return;
        JMessageClient.enterSingleConversation(name);
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("name", name);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        name = getIntent().getStringExtra("name");
        conversation = Conversation.createSingleConversation(name);
        if(conversation==null){
            ToastHelper.showToast(name+"创建对话失败");
        }else {
            ToastHelper.showToast(name+"创建对话成功");
        }
        initView();
        initAdapter();
        initData();
    }

    private void initData() {
        List<Message> allMessage = conversation.getAllMessage();
        List<MessageItem> allMessageItem = new ArrayList<>();
        for (Message message:allMessage){
            allMessageItem.add(new MessageItem(message));
        }
        conversationAdapter.setNewData(allMessageItem);
        mRecyclerView.scrollToPosition(conversationAdapter.getItemCount()-1);
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
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mKeyboard = findViewById(R.id.keyboard);
        mToolbarLeft.setOnClickListener(this);
        mToolbarMiddle.setText(name);
        mKeyboard.setContentView(mRecyclerView);
        mKeyboard.setOnSendListener(new KeyboardView.OnSendListener() {
            @Override
            public void sendText(String text) {
                if (CheckUtil.isNotEmpty(text)) {
                    Message msg = conversation.createSendTextMessage(text);
                    msg.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {
                            initData();
                        }
                    });
                    JMessageClient.sendMessage(msg);
                }
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
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onStop() {
        JMessageClient.exitConversation();
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        if(mKeyboard.hideKeyboardAndPanel()){
            super.onBackPressed();
        }
    }
}
