package com.wadezhang.milkbottle.book;

import android.os.Bundle;
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
    }
}
