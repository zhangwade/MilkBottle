package com.wadezhang.milkbottle.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangxix on 2017/4/14.
 */

public class SearchActivity extends BaseActivity {

    @BindView(R.id.activity_search_tab) TabLayout mTabLayout;
    @BindView(R.id.activity_search_viewpager) ViewPager mViewPager;
    @BindView(R.id.activity_search_text_cancel) TextView mButtonCacel;

    FragmentManager mFragmentManager;
    ArrayList<Fragment> mFragmentList;

    public static void actionStart(Context context){
        context.startActivity(new Intent(context, SearchActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);
        initViewPager();
        mTabLayout.setupWithViewPager(mViewPager);
        mButtonCacel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initViewPager(){
        mFragmentManager = getSupportFragmentManager();
        mFragmentList = new ArrayList<Fragment>();
        Fragment mSearchUserFragment = mFragmentManager.findFragmentByTag(SearchUserFragment.class.getName());
        if(mSearchUserFragment == null) mSearchUserFragment = SearchUserFragment.newInstance();
        Fragment mSearchThemeFragment = mFragmentManager.findFragmentByTag(SearchThemeFragment.class.getName());
        if(mSearchThemeFragment == null) mSearchThemeFragment = SearchThemeFragment.newInstance();
        mFragmentList.add(mSearchUserFragment);
        mFragmentList.add(mSearchThemeFragment);
        mViewPager.setAdapter(new SearchViewPagerAdapter(mFragmentManager, mFragmentList));
    }
}
