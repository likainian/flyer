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
import com.flyer.chat.bean.MessageItem;
import com.flyer.chat.network.IMCallback;
import com.flyer.chat.util.LogUtil;
import com.flyer.chat.util.SharedPreferencesUtil;
import com.flyer.chat.util.ToastUtil;
import com.flyer.chat.widget.KeyboardView;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.jpush.im.android.api.model.Conversation;

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
    private MobIMMessageReceiver mobIMMessageReceiver;
    private Conversation conversation;

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
        conversation = Conversation.createSingleConversation(name);
        if(conversation==null){
            ToastUtil.showToast(name+"创建对话失败");
        }else {
            ToastUtil.showToast(name+"创建对话成功");
        }
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
        MobIM.removeMessageReceiver(mobIMMessageReceiver);
        super.onDestroy();
    }
    private void initReceiver() {
        mobIMMessageReceiver = new MobIMMessageReceiver(){

            @Override
            public void onMessageReceived(List<IMMessage> list) {
                LogUtil.i("ttt","收到消息ConversationActivity");
                initData();
            }

            @Override
            public void onMsgWithDraw(String s, String s1) {

            }
        };
        MobIM.addMessageReceiver(mobIMMessageReceiver);
    }

    private void initData() {
        MobIM.getChatManager().getMessageList(name, IMConversation.TYPE_USER,50,System.currentTimeMillis(),new IMCallback<List<IMMessage>>(){
            @Override
            public void onSuccess(List<IMMessage> imMessages) {
                super.onSuccess(imMessages);
                List<MessageItem> allMessageItem = new ArrayList<>();
                for (IMMessage message:imMessages){
                    allMessageItem.add(0,new MessageItem(message));
                }
                conversationAdapter.setNewData(allMessageItem);
                mRecyclerView.scrollToPosition(conversationAdapter.getItemCount()-1);
            }
        });
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
                IMMessage textMessage = MobIM.getChatManager().createTextMessage(name, text, IMConversation.TYPE_USER);
                MobIM.getChatManager().sendMessage(textMessage, new IMCallback<Void>(){
                    @Override
                    public void onSuccess(Void aVoid) {
                        super.onSuccess(aVoid);
                        ToastUtil.showToast("发送成功");
                        initData();
                    }
                });
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
