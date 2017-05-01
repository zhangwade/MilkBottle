package com.wadezhang.milkbottle.post;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.book.BookContentActivity;
import com.wadezhang.milkbottle.post_detail.PostDetailActivity;
import com.wadezhang.milkbottle.search.SearchActivity;

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
    @BindView(R.id.fragment_post_imagebn_search) ImageButton mButtonSearch;
    @BindView(R.id.fragment_post_text_newpost) TextView mButtonNewPost;

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
        mButtonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BookContentActivity.actionStart(getActivity()); //TODO:修改
            }
        });
        mButtonNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetailActivity.actionStart(getActivity());
            }
        });
        return mView;
    }

    public void initViewPager(){
        mFragmentManager = getChildFragmentManager();
        mViewPagerFragmentList = new ArrayList<Fragment>();
        Fragment mViewPagerFriendFragment = mFragmentManager.findFragmentByTag(PostFriendFragment.class.getName());
        if(mViewPagerFriendFragment == null) mViewPagerFriendFragment = PostFriendFragment.newInstance();
        Fragment mViewPagerFindFragment = mFragmentManager.findFragmentByTag(PostFindFragment.class.getName());
        if(mViewPagerFindFragment == null) mViewPagerFindFragment = PostFindFragment.newInstance();
        mViewPagerFragmentList.add(mViewPagerFriendFragment);
        mViewPagerFragmentList.add(mViewPagerFindFragment);
        mViewPager.setAdapter(new PostViewPagerAdapter(mFragmentManager, mViewPagerFragmentList));
        mViewPager.setCurrentItem(0);
        new PostFriendPresenter((PostFriendFragment)mViewPagerFriendFragment);
        new PostFindPresenter((PostFindFragment)mViewPagerFindFragment);
        mViewPager.addOnPageChangeListener(new onPageChangeListener());
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