/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-2 上午10:11
 */

package com.wadezhang.milkbottle.post_detail;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class PostDetailViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragmentList;

    public PostDetailViewPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentList){
        super(fragmentManager);
        this.mFragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position){
        return mFragmentList.get(position);
    }

    @Override
    public int getCount(){
        return mFragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if(position == 0) return "评论";
            else return "赞";
    }
}
