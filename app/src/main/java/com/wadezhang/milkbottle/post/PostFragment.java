package com.wadezhang.milkbottle.post;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangxix on 2017/3/6.
 */

public class PostFragment extends BaseFragment {

    @BindView(R.id.fragment_post_viewpager) ViewPager mViewPager;
    @BindView(R.id.text_friend_fragment_post) TextView mTopNavigationFriend;
    @BindView(R.id.text_find_fragment_post) TextView mTopNavigationFind;
    ArrayList<Fragment> mViewPagerFragmentList;
    FragmentManager mFragmentManager;

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
        ButterKnife.bind(this, mView);
        initViewPager();
        mTopNavigationFriend.setOnClickListener(new TopNavigationViewPagerOnClickListener());
        mTopNavigationFind.setOnClickListener(new TopNavigationViewPagerOnClickListener());
        mTopNavigationFriend.setTextColor(getResources().getColor(R.color.highLight));
        return mView;
    }

    public void initViewPager(){
        mFragmentManager = getChildFragmentManager();
        mViewPagerFragmentList = new ArrayList<Fragment>();
        Fragment mViewPagerFriendFragment = mFragmentManager.findFragmentByTag(ViewPagerFriendFragment.class.getName());
        if(mViewPagerFriendFragment == null) mViewPagerFriendFragment = ViewPagerFriendFragment.newInstance();
        Fragment mViewPagerFindFragment = mFragmentManager.findFragmentByTag(ViewPagerFindFragment.class.getName());
        if(mViewPagerFindFragment == null) mViewPagerFindFragment = ViewPagerFindFragment.newInstance();
        mViewPagerFragmentList.add(mViewPagerFriendFragment);
        mViewPagerFragmentList.add(mViewPagerFindFragment);
        mViewPager.setAdapter(new ViewPagerAdapter(mFragmentManager));
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new onPageChangeListener());
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

    private class TopNavigationViewPagerOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            switch(view.getId()){
                case R.id.text_friend_fragment_post:
                    if(mViewPager.getCurrentItem() != 0) mViewPager.setCurrentItem(0);
                    break;
                case R.id.text_find_fragment_post:
                    if(mViewPager.getCurrentItem() != 1) mViewPager.setCurrentItem(1);
                    break;
                default:
                    break;
            }
        }
    }

    private int getOldNavigationColor(){
        return getResources().getColor(R.color.dayNavigationColor); //TODO:根据SharedPreferences判断当前主题的默认NavigationColor
    }

    private class onPageChangeListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrollStateChanged(int state){}

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){}

        @Override
        public void onPageSelected(int position){
            switch (position){
                case 0 :
                    mTopNavigationFriend.setTextColor(getResources().getColor(R.color.highLight));
                    mTopNavigationFind.setTextColor(getOldNavigationColor());
                    break;
                case 1 :
                    mTopNavigationFind.setTextColor(getResources().getColor(R.color.highLight));
                    mTopNavigationFriend.setTextColor(getOldNavigationColor());
                    break;
                default:
                    break;
            }
        }
    }
}