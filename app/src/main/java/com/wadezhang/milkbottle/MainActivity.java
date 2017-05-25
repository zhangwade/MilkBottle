/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:52
 */

package com.wadezhang.milkbottle;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lzy.imagepicker.ImagePicker;
import com.wadezhang.milkbottle.book.BookFragment;
import com.wadezhang.milkbottle.bottle.BottleFragment;
import com.wadezhang.milkbottle.me.MeFragment;
import com.wadezhang.milkbottle.message.CheckNewMessageService;
import com.wadezhang.milkbottle.message.MessageFragment;
import com.wadezhang.milkbottle.post.PostFragment;
import com.wadezhang.milkbottle.theme.ThemeFragment;
import com.wadezhang.milkbottle.theme.ThemePresenter;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;

/**
 * Created by zhangxix on 2017/1/24.
 */

public class MainActivity extends BaseActivity {

    @BindView(R.id.bottom_navigation_bar) BottomNavigationBar mBottomNavigationBar;

    android.support.v4.app.Fragment mShowingFragment, mReplaceFragment; //正在显示的Fragment，将要显示的Fragment

    private long clickTime = 0; // 第一次点击的时间

    private ThemePresenter mThemePresenter;

    BadgeItem badgeItem;

    Context mContext;

    private IntentFilter intentFilter;
    private NewMessageReceiver newMessageReceiver;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        //Bmob.initialize(this, "88948b7c6b14026453c55e34644c8b2c");
        //SystemClock.sleep(2000);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        Intent intent = new Intent(this, CheckNewMessageService.class);
        startService(intent);
        if(savedInstanceState == null){
            setDefaultFragment();
        }
        initImagePicker();
        initBottomNavigationBar();
    }

    public void initImagePicker(){
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImagePickerLoader());
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(1);
    }

    private void initBottomNavigationBar(){
        badgeItem = new BadgeItem()
                .setBackgroundColorResource(R.color.highLight)
                .hide();
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        mBottomNavigationBar.addItem(new BottomNavigationItem(R.mipmap.post,R.string.bottom_navigation_bar_1))
                .addItem(new BottomNavigationItem(R.mipmap.theme,R.string.bottom_navigation_bar_2))
                .addItem(new BottomNavigationItem(R.mipmap.message,R.string.bottom_navigation_bar_3).setBadgeItem(badgeItem))
                .addItem(new BottomNavigationItem(R.mipmap.bottle,R.string.bottom_navigation_bar_4))
                .addItem(new BottomNavigationItem(R.mipmap.me,R.string.bottom_navigation_bar_5))
                .setFirstSelectedPosition(0)
                .initialise();
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.wadezhang.milkbottle.NEW_MESSAGE");
        newMessageReceiver = new NewMessageReceiver(badgeItem);
        registerReceiver(newMessageReceiver, intentFilter);
        mBottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position){
                android.support.v4.app.FragmentManager mFragmentManager = getSupportFragmentManager();
                android.support.v4.app.FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
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
                        if(mThemePresenter == null) mThemePresenter = new ThemePresenter((ThemeFragment)mReplaceFragment);
                        break;
                    case 2 :
                        if(!badgeItem.isHidden()) badgeItem.hide();
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

    public class NewMessageReceiver extends BroadcastReceiver{

        BadgeItem mBadgeItem;

        public NewMessageReceiver(BadgeItem badgeItem){
            mBadgeItem = badgeItem;
        }

        @Override
        public void onReceive(Context context, Intent intent){
            if(mBadgeItem.isHidden()) mBadgeItem.show();
            Toast.makeText(mContext, "收到 New Message 广播", Toast.LENGTH_SHORT).show();
            Log.d(getClass().getSimpleName(), "收到 New Message 广播");
        }
    }

    public void setDefaultFragment(){
        android.support.v4.app.FragmentTransaction mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mShowingFragment = PostFragment.newInstance();
        mFragmentTransaction.add(R.id.framelayout_root, mShowingFragment, PostFragment.class.getName()).commit();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Intent intent = new Intent(this, CheckNewMessageService.class);
        stopService(intent);
        unregisterReceiver(newMessageReceiver);
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
            android.os.Process.killProcess(android.os.Process.myPid()); //杀掉当前进程
        }
    }
}
