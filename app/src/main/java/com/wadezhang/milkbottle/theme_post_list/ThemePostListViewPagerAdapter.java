package com.wadezhang.milkbottle.theme_post_list;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class ThemePostListViewPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> mFragmentList;

    public ThemePostListViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList){
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
        if(position == 0) return "最新";
        else return "推荐";
    }
}
