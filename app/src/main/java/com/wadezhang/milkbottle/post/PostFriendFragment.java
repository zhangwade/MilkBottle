package com.wadezhang.milkbottle.post;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

/**
 * Created by zhangxix on 2017/4/6.
 */

public class PostFriendFragment extends BaseFragment {

    public static PostFriendFragment newInstance() {
        PostFriendFragment mPostFriendFragment = new PostFriendFragment();
        return mPostFriendFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_post_viewpager_item, container, false);
        return mView;
    }
}
