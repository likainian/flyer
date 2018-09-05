package com.flyer.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.flyer.chat.R;
import com.flyer.chat.activity.setting.FindFriendActivity;
import com.flyer.chat.adapter.ChatAdapter;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.util.CommonUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

import cn.jpush.im.android.api.JMessageClient;
import cn.jpush.im.android.api.model.Conversation;
import cn.jpush.im.android.api.model.UserInfo;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener {
    private ChatAdapter chatAdapter;
    private List<Conversation> conversationList;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }
    public ImageView mToolbarLeft;
    public TextView mToolbarMiddle;
    public TextView mToolbarRight;
    public SwipeMenuRecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initAdapter();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }
    private void initData(){
        conversationList = JMessageClient.getConversationList();
        chatAdapter.setNewData(conversationList);
    }
    private void initAdapter() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        chatAdapter = new ChatAdapter(getActivity());
        mRecyclerView.setAdapter(chatAdapter);
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getActivity())
                        .setBackgroundColor(CommonUtil.getColor(R.color.red))
                        .setTextColor(CommonUtil.getColor(R.color.white))
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT)
                        .setWidth(100)
                        .setText("删除");
                rightMenu.addMenuItem(deleteItem); // 在Item右侧添加一个菜单。
            }
        };
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator);
        mRecyclerView.setSwipeMenuItemClickListener(new SwipeMenuItemClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge) {
                menuBridge.closeMenu();
                int adapterPosition = menuBridge.getAdapterPosition();
                JMessageClient.deleteSingleConversation(((UserInfo) conversationList.get(adapterPosition).getTargetInfo()).getUserName());
                initData();
            }
        });
    }

    private void initView(View rootView) {
        this.mToolbarLeft = rootView.findViewById(R.id.toolbar_left);
        this.mToolbarMiddle = rootView.findViewById(R.id.toolbar_middle);
        this.mToolbarRight = rootView.findViewById(R.id.toolbar_right);
        this.mRecyclerView = rootView.findViewById(R.id.recycler_view);
        mToolbarMiddle.setText("最近联系");
        mToolbarRight.setBackgroundResource(R.drawable.menu_more);
        mToolbarRight.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.toolbar_right:
                FindFriendActivity.startActivity(getActivity());
                break;
        }
    }
}
