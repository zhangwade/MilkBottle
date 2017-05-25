/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-2 上午10:10
 */

package com.wadezhang.milkbottle.post_detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class PostDetailLikesFragment extends BaseFragment {

    public static PostDetailLikesFragment newInstance() {
        PostDetailLikesFragment mPostDetailLikesFragment = new PostDetailLikesFragment();
        return mPostDetailLikesFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_post_detail_good_item, container, false);
        return mView;
    }
}
