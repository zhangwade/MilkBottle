package com.wadezhang.milkbottle;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangxix on 2017/1/24.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation_bar) BottomNavigationBar mBottomNavigationBar;

    Fragment mShowingFragment, mReplaceFragment; //正在显示的Fragment，将要显示的Fragment

    private long clickTime = 0; // 第一次点击的时间

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayTheme);
        super.onCreate(savedInstanceState);
        ActivityCollector.addActivity(this);
        SystemClock.sleep(2000);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        if(savedInstanceState == null){
            setDefaultFragment();
        }
        initBottomNavigationBar();
    }

    private void initBottomNavigationBar(){
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.ic_3d_rotation_black_24dp,R.string.bottom_navigation_bar_1).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(R.mipmap.ic_3d_rotation_black_24dp,R.string.bottom_navigation_bar_2).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(R.mipmap.ic_3d_rotation_black_24dp,R.string.bottom_navigation_bar_3).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(R.mipmap.ic_3d_rotation_black_24dp,R.string.bottom_navigation_bar_4).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .addItem(new BottomNavigationItem(R.mipmap.ic_3d_rotation_black_24dp,R.string.bottom_navigation_bar_5).setActiveColor(R.color.bottomNavigationBarActiveColor))
                .setFirstSelectedPosition(0)
                .initialise();
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position){//TODO:当重复点击同一Tab时，隐藏再显示可能会有跳转的画面
                FragmentManager mFragmentManager = getFragmentManager();
                FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
                if(mShowingFragment != null){
                    mFragmentTransaction.hide(mShowingFragment);
                }
                switch (position){
                    case 0 :
                        mReplaceFragment = mFragmentManager.findFragmentByTag(PostFragment.class.getName());
                        if(mReplaceFragment == null) mReplaceFragment = PostFragment.newInstance();
                        if(!mReplaceFragment.isAdded())
                            mFragmentTransaction.add(R.id.framelayout_root, mReplaceFragment, PostFragment.class.getName());
                        break;
                    case 1 :
                        mReplaceFragment = mFragmentManager.findFragmentByTag(ThemeFragment.class.getName());
                        if(mReplaceFragment == null) mReplaceFragment = ThemeFragment.newInstance();
                        if(!mReplaceFragment.isAdded())
                            mFragmentTransaction.add(R.id.framelayout_root, mReplaceFragment, ThemeFragment.class.getName());
                        break;
                    case 2 :
                        mReplaceFragment = mFragmentManager.findFragmentByTag(MessageFragment.class.getName());
                        if(mReplaceFragment == null) mReplaceFragment = MessageFragment.newInstance();
                        if(!mReplaceFragment.isAdded())
                            mFragmentTransaction.add(R.id.framelayout_root, mReplaceFragment, MessageFragment.class.getName());
                        break;
                    case 3 :
                        mReplaceFragment = mFragmentManager.findFragmentByTag(BottleFragment.class.getName());
                        if(mReplaceFragment == null) mReplaceFragment = BottleFragment.newInstance();
                        if(!mReplaceFragment.isAdded())
                            mFragmentTransaction.add(R.id.framelayout_root, mReplaceFragment, BottleFragment.class.getName());
                        break;
                    case 4 :
                        mReplaceFragment = mFragmentManager.findFragmentByTag(MeFragment.class.getName());
                        if(mReplaceFragment == null) mReplaceFragment = MeFragment.newInstance();
                        if(!mReplaceFragment.isAdded())
                            mFragmentTransaction.add(R.id.framelayout_root, mReplaceFragment, MeFragment.class.getName());
                        break;
                }
                if(mReplaceFragment.isHidden()) mFragmentTransaction.show(mReplaceFragment);
                mFragmentTransaction.commit();
                mShowingFragment = mReplaceFragment;
            }
            @Override
            public void onTabUnselected(int position){

            }
            @Override
            public void onTabReselected(int position){

            }
        });
    }

    public void setDefaultFragment(){
        FragmentTransaction mFragmentTransaction = getFragmentManager().beginTransaction();
        mShowingFragment = PostFragment.newInstance();
        mFragmentTransaction.add(R.id.framelayout_root, mShowingFragment, PostFragment.class.getName()).commit();
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 是否触发按键为back键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
            return true;
        } else { // 如果不是back键正常响应
            return super.onKeyDown(keyCode, event);
        }
    }
    /* 注意：重写onKeyDown()和onBackPressed()方法都能捕获Back的点击事件，
    onKeyDown()兼容Android 1.0到Android 2.1，也是常规方法，Android 2.0开始又多出了
    一种新的方法onBackPressed()，可以单独获取Back键的按下事件， 方法二的代码将两
    种方法嵌套使用了，onBackPressed()方法会处理返回键的操作，不会向上传播，如果想
    向上传播，则需要使用onKeyDown() */

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(this, "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            ActivityCollector.finishAll();
        }
    }
}
