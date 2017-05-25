/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-27 下午1:27
 */

package com.wadezhang.milkbottle.book;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public class BookContentFragment extends BaseFragment implements BookContentContract.View {

    @BindView(R.id.fragment_book_content_text) TextView mContentText;

    final int FLIP_DISTANCE = 50;
    GestureDetector mGestureDetector;

    int mPageSize = 900;

    private BookContentContract.Presenter mBookContentPresenter;

    public static BookContentFragment newInstance(){
        BookContentFragment mBookContentFragment = new BookContentFragment();
        return mBookContentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGestureDetector = new GestureDetector(getContext(), new Gesture());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_book_content, container, false);
        mView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGestureDetector.onTouchEvent(event);
            }
        });
        ButterKnife.bind(this, mView);
        return mView;
    }

    @Override
    public void onResume(){
        super.onResume();
        //mPageSize = GetWordsLimit.getPageWordsLimit(mContentText.getHeight(), mContentText.getLineHeight(), mContentText.getWidth(), (int)mContentText.getTextSize());
        mBookContentPresenter.setPageSize(mPageSize, mHandler);
        mBookContentPresenter.start();

    }

    public Handler mHandler = new Handler(){

        public void handleMessage(Message message){
            if(message.what == 1) mBookContentPresenter.getPreviousPage();
        }
    };

    @Override
    public void setPresenter(BookContentContract.Presenter presenter){
        mBookContentPresenter = presenter;
    }

    @Override
    public void updateBookContent(String content){
        mContentText.setText(content);
    }

    public class Gesture implements GestureDetector.OnGestureListener{

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2, float velocityX, float velocityY){
            if(event1.getX() - event2.getX() > FLIP_DISTANCE){
                mBookContentPresenter.getNextPage();
                return true;
            }else if(event2.getX() - event1.getX() > FLIP_DISTANCE){
                mBookContentPresenter.getPreviousPage();
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
    }
}
