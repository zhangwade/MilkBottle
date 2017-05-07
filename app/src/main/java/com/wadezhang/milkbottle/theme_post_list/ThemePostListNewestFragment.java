package com.wadezhang.milkbottle.theme_post_list;

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
import com.wadezhang.milkbottle.BaseViewPagerFragment;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.post.Post;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class ThemePostListNewestFragment extends BaseViewPagerFragment implements ThemePostListNewestContract.View {

    @BindView(R.id.activity_theme_post_list_viewpager_swipetoloadlayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRecyclerView;

    private ThemePostListNewestAdapter mThemePostListNewestAdapter;
    private List<Post> mPostList = new ArrayList<>();

    private ThemePostListNewestContract.Presenter mThemePostListNewestPresenter;

    public static ThemePostListNewestFragment newInstance() {
        ThemePostListNewestFragment mThemePostListNewestFragment = new ThemePostListNewestFragment();
        return mThemePostListNewestFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.activity_theme_post_list_viewpager, container, false);
        ButterKnife.bind(this, mView);
        mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mThemePostListNewestAdapter = new ThemePostListNewestAdapter(mPostList);
        mRecyclerView.setAdapter(mThemePostListNewestAdapter);
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
    public void setPresenter(ThemePostListNewestContract.Presenter presenter){
        mThemePostListNewestPresenter = presenter;
    }

    @Override
    public void showToast(String toast){
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateAdapter(List<Post> postList, int actionType){
        if(actionType == 0) mPostList.clear();
        mPostList.addAll(postList);
        mThemePostListNewestAdapter.notifyDataSetChanged();
    }

    public class RefreshListener implements OnRefreshListener {

        @Override
        public void onRefresh(){
            mThemePostListNewestPresenter.getPost(0);
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener {

        @Override
        public void onLoadMore(){
            mThemePostListNewestPresenter.getPost(1);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }
}
