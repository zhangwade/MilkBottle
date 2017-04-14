package com.wadezhang.milkbottle.search;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by zhangxix on 2017/4/14.
 */

public class SearchViewPagerAdapter extends FragmentPagerAdapter {

    ArrayList<Fragment> mFragmentList;

    public SearchViewPagerAdapter(FragmentManager fragmentManager, ArrayList<Fragment> fragmentList){
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
        switch (position) {
            case 0:
                return "用户";
            case 1:
                return "话题";
            default:
                return "用户";
        }
    }
}
