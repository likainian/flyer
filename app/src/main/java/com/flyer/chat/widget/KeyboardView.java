package com.flyer.chat.widget;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.KeyBoardUtil;
import com.flyer.chat.util.SharedPreferencesHelper;

import java.io.File;

/**
 * Created by mike.li on 2018/9/3.
 */

public class KeyboardView extends FrameLayout implements View.OnClickListener, KeyBoardUtil.KeyBoardStatusListener {
    private ImageView mSwitchTextVoice;
    private EditText mKeyboardText;
    private TextView mKeyboardVoice;
    private ImageView mKeyboardEmoticon;
    private ImageView mKeyboardMultimedia;
    private TextView mKeyboardSend;
    private MultimediaView mKeyboardMultimediaLayout;
    private EmoticonView mKeyboardEmoticonLayout;
    private RecyclerView contentView;
    private OnSendListener onSendListener;
    private boolean isShow;

    @Override
    public void onKeyBoardStateChanged(boolean isShow, int height) {
        this.isShow = isShow;
        if(isShow){
            scrollToBottom();
        }else {
            unlockContentViewHeight();
        }
    }

    public interface OnSendListener{
        void sendText(String text);
        void sendFile(File file);
        void sendVoice(File file,int duration);
    }
    public KeyboardView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        Activity activity = (Activity) context;
        KeyBoardUtil.register(activity,this);
    }

    public void setOnSendListener(OnSendListener onSendListener) {
        this.onSendListener = onSendListener;
    }

    public void setContentView(RecyclerView contentView) {
        this.contentView = contentView;
        contentView.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboardAndPanel();
                return false;
            }
        });
    }

    private void initView(final Context context) {
        View.inflate(context, R.layout.keyboard_view, this);
        this.mSwitchTextVoice = findViewById(R.id.switch_text_voice);
        this.mKeyboardText = findViewById(R.id.keyboard_text);
        this.mKeyboardVoice = findViewById(R.id.keyboard_voice);
        this.mKeyboardEmoticon = findViewById(R.id.keyboard_emoticon);
        this.mKeyboardMultimedia = findViewById(R.id.keyboard_multimedia);
        this.mKeyboardSend = findViewById(R.id.keyboard_send);
        this.mKeyboardMultimediaLayout = findViewById(R.id.keyboard_multimedia_layout);
        this.mKeyboardEmoticonLayout = findViewById(R.id.keyboard_emoticon_layout);
        mKeyboardText.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                if(s.length()>0){
                    mKeyboardSend.setVisibility(VISIBLE);
                }else {
                    mKeyboardSend.setVisibility(GONE);
                }
            }
        });
        mKeyboardText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(mKeyboardEmoticonLayout.isShown()||mKeyboardMultimediaLayout.isShown()){
                    lockContentViewHeight();
                    showKeyboard();
                }else {
                    showKeyboard();
                }
                return false;
            }
        });
        mSwitchTextVoice.setOnClickListener(this);
        mKeyboardEmoticon.setOnClickListener(this);
        mKeyboardMultimedia.setOnClickListener(this);
        mKeyboardSend.setOnClickListener(this);
        ViewGroup.LayoutParams layoutParams = mKeyboardMultimediaLayout.getLayoutParams();
        layoutParams.height = SharedPreferencesHelper.getInstance().getKeyboardHight();
        mKeyboardMultimediaLayout.setLayoutParams(layoutParams);
        mKeyboardEmoticonLayout.setLayoutParams(layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.keyboard_send:
                if(onSendListener!=null){
                    onSendListener.sendText(mKeyboardText.getText().toString().trim());
                    mKeyboardText.setText("");
                }
                break;
            case R.id.switch_text_voice:
                unlockContentViewHeight();
                KeyBoardUtil.hideKeyBoard(mKeyboardText);
                mKeyboardEmoticonLayout.setVisibility(GONE);
                mKeyboardMultimediaLayout.setVisibility(GONE);
                if(mKeyboardVoice.isShown()){
                    mKeyboardText.setVisibility(VISIBLE);
                    mKeyboardText.requestFocus();
                    KeyBoardUtil.showKeyBoard(mKeyboardText);
                    mSwitchTextVoice.setImageResource(R.drawable.keyboard_voice);
                    mKeyboardVoice.setVisibility(GONE);
                }else {
                    mSwitchTextVoice.setImageResource(R.drawable.keyboard_text);
                    mKeyboardText.setVisibility(GONE);
                    mKeyboardVoice.setVisibility(VISIBLE);
                }
                break;
            case R.id.keyboard_emoticon:
                if(isShow){
                    lockContentViewHeight();
                    showEmoticon();
                }else {
                    showEmoticon();
                }
                break;
            case R.id.keyboard_multimedia:
                if(isShow){
                    lockContentViewHeight();
                    showMultimedia();
                }else {
                    showMultimedia();
                }
                break;
        }

    }

    private void showKeyboard(){
        mKeyboardEmoticonLayout.setVisibility(GONE);
        mKeyboardMultimediaLayout.setVisibility(GONE);
    }
    private void showMultimedia(){
        KeyBoardUtil.hideKeyBoard(mKeyboardText);
        mKeyboardEmoticonLayout.setVisibility(GONE);
        mKeyboardMultimediaLayout.setVisibility(VISIBLE);
        scrollToBottom();
    }
    private void showEmoticon(){
        KeyBoardUtil.hideKeyBoard(mKeyboardText);
        mKeyboardMultimediaLayout.setVisibility(GONE);
        mKeyboardEmoticonLayout.setVisibility(VISIBLE);
        scrollToBottom();
    }

    private void lockContentViewHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = contentView.getHeight();
        layoutParams.weight = 0;
    }
    private void unlockContentViewHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.weight = 1;
    }
    private void scrollToBottom(){
        contentView.requestLayout();
        contentView.post(new Runnable() {
            @Override
            public void run() {
                contentView.scrollToPosition(contentView.getAdapter().getItemCount()-1);
            }
        });
    }
    public boolean hideKeyboardAndPanel(){
        boolean isShowKeyboardAndPanel = !(isShow||mKeyboardEmoticonLayout.isShown()||mKeyboardMultimediaLayout.isShown());
        unlockContentViewHeight();
        KeyBoardUtil.hideKeyBoard(mKeyboardText);
        mKeyboardEmoticonLayout.setVisibility(GONE);
        mKeyboardMultimediaLayout.setVisibility(GONE);
        return isShowKeyboardAndPanel;
    }
}
