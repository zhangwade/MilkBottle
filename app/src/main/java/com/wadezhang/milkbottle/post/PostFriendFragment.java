package com.wadezhang.milkbottle.post;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wadezhang.milkbottle.BaseViewPagerFragment;
import com.wadezhang.milkbottle.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangxix on 2017/4/6.
 */

public class PostFriendFragment extends BaseViewPagerFragment implements PostFriendContract.View {

    @BindView(R.id.fragment_post_viewpager_swipe_to_load_layout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRecyclerView;

    private PostFriendAdapter mPostFriendAdapter;
    private List<Post> mPostList = new ArrayList<>();

    private PostFriendContract.Presenter mPostFriendPresenter;

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
        ButterKnife.bind(this, mView);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostFriendAdapter = new PostFriendAdapter(mPostList);
        mRecyclerView.setAdapter(mPostFriendAdapter);
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
        return mView;
    }

    @Override
    public void lazyFetchData(){

    }

    @Override
    public void setPresenter(PostFriendContract.Presenter presenter){
        mPostFriendPresenter = presenter;
    }
}
