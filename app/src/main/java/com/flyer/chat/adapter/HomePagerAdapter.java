package com.flyer.chat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by mike.li on 2018/5/11.
 * FragmentPagerAdapter仅仅将该fragment从页面中detach掉，fragment还是在manager中保存，内存没有被释放
 */

public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    public HomePagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
