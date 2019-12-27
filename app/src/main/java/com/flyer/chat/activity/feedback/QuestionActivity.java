package com.flyer.chat.activity.feedback;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.common.adapter.GridPhotoAdapter;
import com.flyer.chat.activity.feedback.bean.Question;
import com.flyer.chat.base.ToolbarActivity;
import com.flyer.chat.listener.EditTextWatcher;
import com.flyer.chat.util.BitmapUtil;
import com.flyer.chat.util.CheckUtil;
import com.flyer.chat.util.GlideEngine;
import com.flyer.chat.util.ItemTouchCallback;
import com.flyer.chat.util.ToastUtil;
import com.huantansheng.easyphotos.EasyPhotos;
import com.huantansheng.easyphotos.callback.SelectCallback;
import com.huantansheng.easyphotos.models.album.entity.Photo;
import com.huantansheng.easyphotos.setting.Setting;

import java.util.ArrayList;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by mike.li on 2019/10/29.
 */
public class QuestionActivity extends ToolbarActivity{
    public static final String QUESTION_TYPE = "QUESTION_TYPE";
    public static final String NORMAL_TYPE = "NORMAL_TYPE";//简单问题
    public static final String UI_TYPE = "UI_TYPE";//布局UI
    public static final String NEW_TYPE = "NEW_TYPE";//新功能
    public static final String FUN_TYPE = "FUN_TYPE";//不合理
    public static final String BUG_TYPE = "BUG_TYPE";//bug
    private EditText mEtMessage;
    private EditText mEtContact;
    private TextView mTvCommit;
    private GridPhotoAdapter gridPhotoAdapter;

    public static void startActivity(Context context,String type) {
        context.startActivity(new Intent(context, QuestionActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        initView();
        setToolbarMiddleText("遇到问题");
    }

    private void initView() {
        mEtMessage = (EditText) findViewById(R.id.et_message);
        mEtContact = (EditText) findViewById(R.id.et_contact);
        mTvCommit = (TextView) findViewById(R.id.tv_commit);
        TextView mTvDelete = (TextView) findViewById(R.id.tv_delete);
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mTvDelete.setVisibility(View.GONE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,3));
        gridPhotoAdapter = new GridPhotoAdapter(this,9);
        new ItemTouchHelper(new ItemTouchCallback(gridPhotoAdapter,gridPhotoAdapter.getData(), mTvDelete)).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(gridPhotoAdapter);
        gridPhotoAdapter.setOnClickAddListener(new GridPhotoAdapter.OnClickAddListener() {
            @Override
            public void OnClickAdd() {
                addPicture();
            }
        });

        mEtMessage.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void OnTextChange(Editable s) {
                mTvCommit.setEnabled(CheckUtil.isNotEmpty(mEtMessage.getText().toString().trim()));
            }
        });
        mTvCommit.setEnabled(CheckUtil.isNotEmpty(mEtMessage.getText().toString().trim()));
        mTvCommit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                commit();
            }
        });
    }

    private void addPicture(){
        EasyPhotos.createAlbum(this, true, GlideEngine.getInstance())
                .setFileProviderAuthority("cn.bmob.v3.util.BmobContentProvider")
                .setCameraLocation(Setting.LIST_FIRST)
                .setCount(9)
                .setSelectedPhotoPaths((ArrayList<String>) gridPhotoAdapter.getData())
                .start(new SelectCallback() {
                    @Override
                    public void onResult(ArrayList<Photo> photos, ArrayList<String> paths, boolean isOriginal) {
                        gridPhotoAdapter.setNewData(paths);
                    }
                });
    }

    private void commit(){
        Question question = new Question();
        ArrayList<String> images = new ArrayList<>();
        for (String file:gridPhotoAdapter.getData()) {
            String image = BitmapUtil.bitmapToBase64(BitmapFactory.decodeFile(file));
            images.add(image);
        }
        question.setMessage(mEtMessage.getText().toString().trim());
        question.setContact(mEtContact.getText().toString().trim());
        question.setImages(images);
        showLoadingDialog();
        question.save(new SaveListener<String>() {
            @Override
            public void done(String objectId,BmobException e) {
                closeLoadingDialog();
                if(e==null){
                    ToastUtil.showToast("提交成功");
                }else{
                    ToastUtil.showToast("提交失败" + e.getMessage());
                }
            }
        });
    }
}
