package com.wadezhang.milkbottle.theme_post_list;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme.Theme;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class ThemePostListActivity extends BaseActivity {

    @BindView(R.id.activity_theme_post_list_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_theme_post_list_imgbtn_detail) ImageButton mBtnDetail;
    @BindView(R.id.activity_theme_post_list_text_title) TextView mTitle;
    @BindView(R.id.activity_theme_post_list_tablayout) TabLayout mTabLayout;
    @BindView(R.id.activity_theme_post_list_viewpager) ViewPager mViewPager;
    @BindView(R.id.activity_theme_post_list_text_newpost) TextView mNewPost;
    @BindView(R.id.activity_theme_post_list_text_sendpost) TextView mSendPost;

    String mThemeId;
    String mThemeName;

    FragmentManager mFragmentManager;
    List<Fragment> mFragmentList;

    public static void actionStart(Context context, Theme theme){
        Intent intent = new Intent(context, ThemePostListActivity.class);
        intent.putExtra("themeId", theme.getObjectId());
        intent.putExtra("themeName", theme.getName());
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme_post_list);
        ButterKnife.bind(this);
        init();
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
    }

    public void init(){
        Intent mIntent = getIntent();
        mThemeId = mIntent.getStringExtra("themeId");
        mThemeName = mIntent.getStringExtra("themeName");
        mTitle.setText(mThemeName);
    }

    public void initViewPager(){
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<>();
        Fragment mThemePostListNewestFragment = mFragmentManager.findFragmentByTag(ThemePostListNewestFragment.class.getName());
        if(mThemePostListNewestFragment == null) mThemePostListNewestFragment = ThemePostListNewestFragment.newInstance();
        Fragment mThemePostListHotFragment = mFragmentManager.findFragmentByTag(ThemePostListHotFragment.class.getName());
        if(mThemePostListHotFragment == null) mThemePostListHotFragment = ThemePostListHotFragment.newInstance();
        mFragmentList.add(mThemePostListNewestFragment);
        mFragmentList.add(mThemePostListHotFragment);
        mViewPager.setAdapter(new ThemePostListViewPagerAdapter(mFragmentManager, mFragmentList));
        mViewPager.setCurrentItem(0);
        new ThemePostListNewestPresenter((ThemePostListNewestFragment)mThemePostListNewestFragment, mThemeId);
        new ThemePostListHotPresenter((ThemePostListHotFragment)mThemePostListHotFragment, mThemeId);
    }
}
