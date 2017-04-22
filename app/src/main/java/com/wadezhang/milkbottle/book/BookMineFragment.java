package com.wadezhang.milkbottle.book;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

/**
 * Created by Administrator on 2017/4/22 0022.
 */

public class BookMineFragment extends BaseFragment {

    public static BookMineFragment newInstance() {
        BookMineFragment mBookMineFragment = new BookMineFragment();
        return mBookMineFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_book_viewpager, container, false);
        return mView;
    }
}
