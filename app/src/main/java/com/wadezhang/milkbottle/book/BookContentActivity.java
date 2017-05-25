/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-27 上午10:18
 */

package com.wadezhang.milkbottle.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class BookContentActivity extends BaseActivity {

    //final int FLIP_DISTANCE = 50;

    //GestureDetector mGestureDetector;

    public static void actionStart(Context context){
        context.startActivity(new Intent(context, BookContentActivity.class));
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_book_content);
        //mGestureDetector = new GestureDetector(this, new Gesture());
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment mBookContentFragment = mFragmentManager.findFragmentByTag(BookContentFragment.class.getName());
        if(mBookContentFragment == null) mBookContentFragment = BookContentFragment.newInstance();
        mFragmentTransaction.add(R.id.activity_book_content_framelayout, mBookContentFragment, BookContentFragment.class.getName());
        mFragmentTransaction.commit();
        new BookContentPresenter((BookContentFragment)mBookContentFragment);
    }
/*
    @Override
    public boolean onTouchEvent(MotionEvent event){
        return mGestureDetector.onTouchEvent(event);
    }

    public class Gesture implements GestureDetector.OnGestureListener{

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event1.getX() - event2.getX() > FLIP_DISTANCE){
                return true;
            }else if(event2.getX() - event1.getX() > FLIP_DISTANCE){
                return true;
            }
            return false;
        }

        @Override
        public boolean onDown(MotionEvent event){
            return false;
        }

        @Override
        public void onLongPress(MotionEvent event){}

        @Override
        public boolean onScroll(MotionEvent event1, MotionEvent event2, float arg1, float arg2){
            return false;
        }

        @Override
        public void onShowPress(MotionEvent event){}

        @Override
        public boolean onSingleTapUp(MotionEvent event){
            return false;
        }
    } */
}
