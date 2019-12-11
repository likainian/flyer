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
import com.flyer.chat.network.IMCallback;
import com.flyer.chat.util.CommonUtil;
import com.flyer.chat.util.LogUtil;
import com.mob.imsdk.MobIM;
import com.mob.imsdk.MobIMMessageReceiver;
import com.mob.imsdk.MobIMReceiver;
import com.mob.imsdk.model.IMConversation;
import com.mob.imsdk.model.IMMessage;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.BezierRadarHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.List;

/**
 * Created by mike.li on 2018/7/9.
 */

public class ChatFragment extends BaseFragment implements View.OnClickListener {
    private ChatAdapter chatAdapter;
    private List<IMConversation> conversationList;
    private MobIMMessageReceiver mobIMMessageReceiver;
    private MobIMReceiver mobIMReceiver;

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }
    public ImageView mToolbarLeft;
    public TextView mToolbarMiddle;
    public TextView mToolbarRight;
    public TextView mImStatus;
    public SwipeMenuRecyclerView mRecyclerView;
    public SmartRefreshLayout mSmartRefresh;

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
        initReceiver();
    }

    private void initReceiver() {
        mobIMMessageReceiver = new MobIMMessageReceiver(){

            @Override
            public void onMessageReceived(List<IMMessage> list) {
                LogUtil.i("ttt","收到消息ChatFragment");
                initData();
            }

            @Override
            public void onMsgWithDraw(String s, String s1) {

            }
        };
        mobIMReceiver = new MobIMReceiver() {
            @Override
            public void onConnected() {
                LogUtil.i("ttt","onConnected");
                mImStatus.setVisibility(View.GONE);
                initData();
            }

            @Override
            public void onConnecting() {
                mImStatus.setVisibility(View.VISIBLE);
                mImStatus.setText("连接中。。。");
                LogUtil.i("ttt","onConnecting");
            }

            @Override
            public void onDisconnected(int i) {
                mImStatus.setVisibility(View.VISIBLE);
                mImStatus.setText("断开连接");
                LogUtil.i("ttt","onDisconnected"+i);
            }
        };
        MobIM.addMessageReceiver(mobIMMessageReceiver);
        MobIM.addGeneralReceiver(mobIMReceiver);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onDestroy() {
        MobIM.removeMessageReceiver(mobIMMessageReceiver);
        MobIM.removeGeneralReceiver(mobIMReceiver);
        super.onDestroy();
    }

    private void initData(){
        MobIM.getChatManager().getAllLocalConversations(new IMCallback<List<IMConversation>>(){
            @Override
            public void onSuccess(List<IMConversation> imConversations) {
                super.onSuccess(imConversations);
                mSmartRefresh.finishRefresh();
                LogUtil.i("ttt",imConversations.toString());
                conversationList = imConversations;
                chatAdapter.setNewData(imConversations);
            }


            @Override
            public void onError(int i, String s) {
                mSmartRefresh.finishRefresh();
                super.onError(i, s);
                LogUtil.i("ttt",s);
            }
        });
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
                MobIM.getChatManager().delConversation(conversationList.get(adapterPosition).getId(),conversationList.get(adapterPosition).getType());
                initData();
            }
        });
    }

    private void initView(View rootView) {
        this.mToolbarLeft = rootView.findViewById(R.id.toolbar_left);
        this.mToolbarMiddle = rootView.findViewById(R.id.toolbar_middle);
        this.mToolbarRight = rootView.findViewById(R.id.toolbar_right);
        this.mImStatus = rootView.findViewById(R.id.im_status);
        this.mRecyclerView = rootView.findViewById(R.id.recycler_view);
        this.mSmartRefresh = rootView.findViewById(R.id.smart_refresh);
        mToolbarMiddle.setText("最近联系");
        mToolbarRight.setBackgroundResource(R.drawable.menu_more);
        mToolbarRight.setOnClickListener(this);
        mSmartRefresh.setRefreshHeader(new BezierRadarHeader(getActivity()).setAccentColorId(R.color.colorPrimaryDark).setPrimaryColorId(R.color.light_gray));
        mSmartRefresh.setEnableRefresh(true);
        mSmartRefresh.setEnableLoadMore(false);
        mSmartRefresh.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }

            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                initData();
            }
        });
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
