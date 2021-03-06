/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-17 下午11:39
 */

package com.wadezhang.milkbottle.post;

import android.content.Context;
import android.view.View;

import com.wadezhang.milkbottle.post_detail.PostDetailActivity;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class OnClickListenerToPostDetail implements View.OnClickListener {

    private Context mContext;
    private Post mPost;
    private boolean mIsLikes;

    public OnClickListenerToPostDetail(Context context, Post post, boolean isLikes){
        mContext = context;
        mPost = post;
        mIsLikes = isLikes;
    }

    @Override
    public void onClick(View view){
        PostDetailActivity.actionStart(mContext, mPost, null);
    }
}
