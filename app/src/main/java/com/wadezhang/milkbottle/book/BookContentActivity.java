package com.wadezhang.milkbottle.book;

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

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //无title
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  //全屏
        setContentView(R.layout.activity_book_content);
        //mGestureDetector = new GestureDetector(this, new Gesture());
        FragmentManager mFragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager.beginTransaction();
        Fragment mBookContentFragment = mFragmentManager.findFragmentByTag(BookContentFragment.class.getName());
        if(mBookContentFragment == null) mBookContentFragment = BookShopFragment.newInstance();
        mFragmentTransaction.add(R.id.activity_book_content_framelayout, mBookContentFragment, BookContentFragment.class.getName());
        mFragmentTransaction.commit();
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
