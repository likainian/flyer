package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.adapter.ConversationAdapter;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.ToastHelper;

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
    private EditText mMessageText;
    private TextView mMessageSend;
    private String name;

    public static void startActivity(Context context, String name) {
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
    }

    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Message> allMessage = conversation.getAllMessage();
        mRecyclerView.setAdapter(new ConversationAdapter(allMessage));
    }

    private void initView() {
        mToolbarLeft = findViewById(R.id.toolbar_left);
        mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mToolbarRight = findViewById(R.id.toolbar_right);
        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        mMessageText = findViewById(R.id.message_text);
        mMessageSend = findViewById(R.id.message_send);
        mToolbarLeft.setOnClickListener(this);
        mMessageSend.setOnClickListener(this);
        mToolbarMiddle.setText(name);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.message_send:
                if(CheckUtil.isNotEmpty(mMessageText.getText().toString().trim())){
                    Message msg = conversation.createSendTextMessage(mMessageText.getText().toString().trim());
                    msg.setOnSendCompleteCallback(new BasicCallback() {
                        @Override
                        public void gotResult(int i, String s) {

                        }
                    });
                    JMessageClient.sendMessage(msg);
                }
                break;
        }
    }

    @Override
    protected void onStop() {
        JMessageClient.exitConversation();
        super.onStop();
    }
}
