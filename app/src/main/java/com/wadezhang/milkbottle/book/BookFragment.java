package com.wadezhang.milkbottle.book;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

public class BookFragment extends BaseFragment {

    @BindView(R.id.fragment_book_viewpager) ViewPager mViewPager;
    @BindView(R.id.fragment_book_text_shop) TextView mTopNavigationShop;
    @BindView(R.id.fragment_book_text_mine) TextView mTopNavigationMine;

    ArrayList<Fragment> mViewPagerFragmentList;
    FragmentManager mFragmentManager;

    public static BookFragment newInstance() {
        BookFragment mBookFragment = new BookFragment();
        return mBookFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_book, container, false);
        ButterKnife.bind(this, mView);
        initViewPager();
        mTopNavigationShop.setOnClickListener(new TopNavigationViewPagerOnClickListener());
        mTopNavigationMine.setOnClickListener(new TopNavigationViewPagerOnClickListener());
        mTopNavigationShop.setTextColor(getResources().getColor(R.color.highLight));
        return mView;
    }

    public void initViewPager(){
        mFragmentManager = getChildFragmentManager();
        mViewPagerFragmentList = new ArrayList<Fragment>();
        Fragment mViewPagerShopFragment = mFragmentManager.findFragmentByTag(BookShopFragment.class.getName());
        if(mViewPagerShopFragment == null) mViewPagerShopFragment = BookShopFragment.newInstance();
        Fragment mViewPagerMineFragment = mFragmentManager.findFragmentByTag(BookMineFragment.class.getName());
        if(mViewPagerMineFragment == null) mViewPagerMineFragment = BookMineFragment.newInstance();
        mViewPagerFragmentList.add(mViewPagerShopFragment);
        mViewPagerFragmentList.add(mViewPagerMineFragment);
        mViewPager.setAdapter(new BookViewPagerAdapter(mFragmentManager, mViewPagerFragmentList));
        mViewPager.setCurrentItem(0);
        mViewPager.addOnPageChangeListener(new onPageChangeListener());
    }

    private class TopNavigationViewPagerOnClickListener implements View.OnClickListener{

        @Override
        public void onClick(View view){
            switch(view.getId()){
                case R.id.fragment_book_text_shop:
                    if(mViewPager.getCurrentItem() != 0) mViewPager.setCurrentItem(0);
                    break;
                case R.id.fragment_book_text_mine:
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
                    mTopNavigationShop.setTextColor(getResources().getColor(R.color.highLight));
                    mTopNavigationMine.setTextColor(getOldNavigationColor());
                    break;
                case 1 :
                    mTopNavigationMine.setTextColor(getResources().getColor(R.color.highLight));
                    mTopNavigationShop.setTextColor(getOldNavigationColor());
                    break;
                default:
                    break;
            }
        }
    }
}
