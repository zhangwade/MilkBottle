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

public class PostDetailCommentFragment extends BaseFragment {

    public static PostDetailCommentFragment newInstance() {
        PostDetailCommentFragment mPostDetailCommentFragment = new PostDetailCommentFragment();
        return mPostDetailCommentFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_post_detail_comment_item, container, false);
        return mView;
    }
}
