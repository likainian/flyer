package com.flyer.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flyer.chat.R;
import com.flyer.chat.base.BaseFragment;

/**
 * Created by mike.li on 2018/9/3.
 */

public class EmoticonFragment extends BaseFragment{
    public static EmoticonFragment newInstance() {
        return new EmoticonFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_emoticon,null);
    }
}
