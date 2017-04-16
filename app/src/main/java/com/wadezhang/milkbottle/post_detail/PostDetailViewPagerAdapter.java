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
        switch (position) {
            case 0 :
                return "推荐";
            case 1 :
                return "评论";
            case 2 :
                return "赞";
            default:
                return "推荐";
        }
    }
}
