/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-3 上午11:58
 */

package com.wadezhang.milkbottle.post_detail;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.BaseViewPagerFragment;
import com.wadezhang.milkbottle.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/16 0016.
 */

public class PostDetailCommentFragment extends BaseViewPagerFragment implements PostDetailCommentContract.View {

    //@BindView(R.id.activity_post_detail_viewpager_swipetoloadlayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.activity_post_detail_viewpager_recyclerview) RecyclerView mRecyclerView;

    private PostDetailCommentAdapter mPostDetailCommentAdapter;
    private List<Comment> mCommentList = new ArrayList<>();

    public PostDetailCommentContract.Presenter mPostDetailCommentPresenter;

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
        View mView = inflater.inflate(R.layout.activity_post_detail_viewpager, container, false);
        ButterKnife.bind(this, mView);
        //mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        //mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostDetailCommentAdapter = new PostDetailCommentAdapter(mCommentList);
        mRecyclerView.setAdapter(mPostDetailCommentAdapter);
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
        return mView;
    }

    @Override
    public void lazyFetchData(){ //首次点开该页面拉取数据
        /*
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });  */
        mPostDetailCommentPresenter.getComment(0);
    }

    @Override
    public void setPresenter(PostDetailCommentContract.Presenter presenter){
        mPostDetailCommentPresenter = presenter;
    }

    @Override
    public void showToast(String toast){
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAdapter(List<Comment> postList, int actionType){
        if(actionType == 0) mCommentList.clear();
        mCommentList.addAll(postList);
        mPostDetailCommentAdapter.notifyDataSetChanged();
    }
/*
    public class RefreshListener implements OnRefreshListener {

        @Override
        public void onRefresh(){
            mPostDetailCommentPresenter.getComment(0);
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener {

        @Override
        public void onLoadMore(){
            mPostDetailCommentPresenter.getComment(1);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }   */
}
