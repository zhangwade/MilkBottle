package com.wadezhang.milkbottle;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by zhangxix on 2017/3/6.
 */

public class PostFragment extends BaseFragment {

    @BindView(R.id.fragment_post_viewpager) ViewPager mViewPager;
    ArrayList<Fragment> mViewPagerFragmentList;

    public static PostFragment newInstance() {
        PostFragment mPostFragment = new PostFragment();
        return mPostFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_post, container, false);
        initViewPager();
        return mView;
    }

    public void initViewPager(){
        mViewPagerFragmentList = new ArrayList<Fragment>();
        Fragment mVierPagerFriendsFragment = new VierPagerFriendsFragment();
        Fragment mVierPagerFindFragment = new VierPagerFindFragment();
        mViewPagerFragmentList.add(mVierPagerFriendsFragment);
        mViewPagerFragmentList.add(mVierPagerFindFragment);
        mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager()));
        mViewPager.setCurrentItem(0);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        public ViewPagerAdapter(android.support.v4.app.FragmentManager fragmentManager){
            super(fragmentManager);
        }

        @Override
        public Fragment getItem(int position){
            return mViewPagerFragmentList.get(position);
        }

        @Override
        public int getCount(){
            return mViewPagerFragmentList.size();
        }
    }
}