package com.wadezhang.milkbottle.post;

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
 * Created by zhangxix on 2017/4/6.
 */

public class PostFindFragment extends BaseViewPagerFragment implements PostFindContract.View {

    @BindView(R.id.fragment_post_viewpager_swipe_to_load_layout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRecyclerView;

    private PostFindAdapter mPostFindAdapter;
    private List<Post> mPostList = new ArrayList<>();

    private PostFindContract.Presenter mPostFindPresenter;

    public static PostFindFragment newInstance() {
        PostFindFragment mPostFindFragment = new PostFindFragment();
        return mPostFindFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_post_viewpager, container, false);
        ButterKnife.bind(this, mView);
        mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostFindAdapter = new PostFindAdapter(mPostList);
        mRecyclerView.setAdapter(mPostFindAdapter);
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
        return mView;
    }

    @Override
    public void lazyFetchData(){ //首次点开该页面拉取数据
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void setPresenter(PostFindContract.Presenter presenter){
        mPostFindPresenter = presenter;
    }

    @Override
    public void showToast(String toast){
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAdapter(List<Post> postList, int actionType){
        if(actionType == 0) mPostList.clear();
        mPostList.addAll(postList);
        mPostFindAdapter.notifyDataSetChanged();
    }

    public class RefreshListener implements OnRefreshListener {

        @Override
        public void onRefresh(){
            mPostFindPresenter.getPost(0);
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener {

        @Override
        public void onLoadMore(){
            mPostFindPresenter.getPost(1);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }
}
