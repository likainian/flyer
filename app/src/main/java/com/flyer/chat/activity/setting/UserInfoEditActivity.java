package com.flyer.chat.activity.setting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseActivity;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.KeyBoardUtil;

/**
 * Created by mike.li on 2018/8/23.
 */

public class UserInfoEditActivity extends BaseActivity implements View.OnClickListener {
    private TextView mToolbarRight;
    private EditText mEdit;

    public static void startActivityForResult(Activity context, String title, String name,int request) {
        Intent intent = new Intent(context, UserInfoEditActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("name", name);
        context.startActivityForResult(intent,request);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info_edit);
        initView();
    }

    private void initView() {
        ImageView mToolbarLeft = findViewById(R.id.toolbar_left);
        TextView mToolbarMiddle = findViewById(R.id.toolbar_middle);
        mToolbarRight = findViewById(R.id.toolbar_right);
        mEdit = findViewById(R.id.edit);
        mToolbarLeft.setOnClickListener(this);
        String title = getIntent().getStringExtra("title");
        mToolbarMiddle.setText(title);
        mToolbarRight.setText("完成");
        mToolbarRight.setOnClickListener(this);
        mToolbarRight.setTextColor(CommonUtil.getColor(R.color.selector_enble_color));
        mEdit.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                if(CheckUtil.isEmpty(s.toString().trim())){
                    mToolbarRight.setEnabled(false);
                }else {
                    mToolbarRight.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_left:
                onBackPressed();
                break;
            case R.id.toolbar_right:
                Intent intent = new Intent();
                intent.putExtra("name",mEdit.getText().toString().trim());
                setResult(Activity.RESULT_OK,intent);
                KeyBoardUtil.hideKeyBoard(mEdit);
                onBackPressed();
                break;
        }
    }
}
