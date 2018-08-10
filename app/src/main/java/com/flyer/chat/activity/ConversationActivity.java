package com.flyer.chat.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.util.CheckUtil;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.Message;
import cn.jpush.im.android.api.options.MessageSendingOptions;

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

    public static void startActivity(Context context, String udid) {
        Intent intent = new Intent(context, ConversationActivity.class);
        intent.putExtra("udid", udid);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initView();
        String udid = getIntent().getStringExtra("udid");
        conversation = Conversation.createSingleConversation(udid, "eaa55c30696293983d30fa74");
        if(conversation==null){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
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
                    MessageSendingOptions options = new MessageSendingOptions();
                    options.setNeedReadReceipt(true);
                    JMessageClient.sendMessage(msg, options);
                }
                break;
        }
    }
}
