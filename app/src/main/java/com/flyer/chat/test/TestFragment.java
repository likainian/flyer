package com.flyer.chat.test;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseFragment;
import com.flyer.chat.test.adapter.TestAdapter;
import com.flyer.chat.test.bean.TestBean;

import java.util.ArrayList;

/**
 * Created by mike.li on 2020/6/18.
 */
public class TestFragment extends BaseFragment {


    public static TestFragment newInstance() {

        Bundle args = new Bundle();
        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
    }


    private void initView(View rootView) {

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),3));
        ArrayList<TestBean> testBeans = new ArrayList<>();
        testBeans.add(new TestBean("banner组件",R.drawable.ic_launcher));
        testBeans.add(new TestBean("日期组件",R.drawable.ic_launcher));
        testBeans.add(new TestBean("日历组件",R.drawable.ic_launcher));
        testBeans.add(new TestBean("图片处理",R.drawable.ic_launcher));
        testBeans.add(new TestBean("侧滑组件",R.drawable.ic_launcher));
        testBeans.add(new TestBean("自绘组件",R.drawable.ic_launcher));
        testBeans.add(new TestBean("上下拉刷新组件",R.drawable.ic_launcher));
        testBeans.add(new TestBean("弹窗",R.drawable.ic_launcher));
        testBeans.add(new TestBean("扫码",R.drawable.ic_launcher));

        testBeans.add(new TestBean("直播",R.drawable.ic_launcher));
        testBeans.add(new TestBean("音乐和视频播放",R.drawable.ic_launcher));
        testBeans.add(new TestBean("人脸识别",R.drawable.ic_launcher));
        testBeans.add(new TestBean("wifi和定位",R.drawable.ic_launcher));
        testBeans.add(new TestBean("通知和推送",R.drawable.ic_launcher));
        testBeans.add(new TestBean("朋友圈拖拽",R.drawable.ic_launcher));

        testBeans.add(new TestBean("统计和报错分析",R.drawable.ic_launcher));
        testBeans.add(new TestBean("支付",R.drawable.ic_launcher));
        testBeans.add(new TestBean("分享",R.drawable.ic_launcher));
        TestAdapter testAdapter = new TestAdapter(getActivity(),testBeans);
        mRecyclerView.setAdapter(testAdapter);


    }


}
