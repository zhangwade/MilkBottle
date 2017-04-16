package com.wadezhang.milkbottle.post_detail;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageButton;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class PostDetailActivity extends BaseActivity {

    @BindView(R.id.activity_post_detail_tab) TabLayout mTabLayout;
    @BindView(R.id.activity_post_detail_viewpager) ViewPager mViewPager;
    @BindView(R.id.activity_post_detail_button_back) ImageButton mButtonBack;

    FragmentManager mFragmentManager;
    ArrayList<Fragment> mFragmentList;

    public static void actionStart(Context context){
        context.startActivity(new Intent(context, PostDetailActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initViewPager(){
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<Fragment>();
        Fragment mPostDetailRecommendFragment = mFragmentManager.findFragmentByTag(PostDetailRecommendFragment.class.getName());
        if(mPostDetailRecommendFragment == null) mPostDetailRecommendFragment = PostDetailRecommendFragment.newInstance();
        Fragment mPostDetailCommentFragment = mFragmentManager.findFragmentByTag(PostDetailCommentFragment.class.getName());
        if(mPostDetailCommentFragment == null) mPostDetailCommentFragment = PostDetailCommentFragment.newInstance();
        Fragment mPostDetailGoodFragment = mFragmentManager.findFragmentByTag(PostDetailGoodFragment.class.getName());
        if(mPostDetailGoodFragment == null) mPostDetailGoodFragment = PostDetailGoodFragment.newInstance();
        mFragmentList.add(mPostDetailRecommendFragment);
        mFragmentList.add(mPostDetailCommentFragment);
        mFragmentList.add(mPostDetailGoodFragment);
        mViewPager.setAdapter(new PostDetailViewPagerAdapter(mFragmentManager, mFragmentList));
    }
}
