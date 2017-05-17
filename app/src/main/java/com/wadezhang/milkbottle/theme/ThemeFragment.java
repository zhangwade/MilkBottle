package com.wadezhang.milkbottle.theme;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.new_theme.NewThemeActivity;
import com.wadezhang.milkbottle.search.SearchThemeActivity;
import com.wadezhang.milkbottle.theme_category.ThemeCategoryListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by zhangxix on 2017/3/6.
 */

public class ThemeFragment extends BaseFragment implements ThemeContract.View{

    @BindView(R.id.fragment_theme_text_create_theme) TextView mCreateTheme;
    @BindView(R.id.fragment_theme_text_search) TextView mSearch;
    @BindView(R.id.fragment_theme_text_category) TextView mCategory;
    @BindView(R.id.fragment_theme_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.fragment_theme_swipe_refresh) SwipeRefreshLayout mSwipeRefreshLayout;

    private ThemeAdapter mThemeAdapter;
    private List<Theme> mThemeList = new ArrayList<>();
    private int mHotItem = 6;  //取 6 条热门话题
    private int mNewestItem = 30;  //取 30 条最新话题
    private int mSecondGroupPosition;

    View mView;

    private ThemeContract.Presenter mThemePresenter;

    public static ThemeFragment newInstance() {
        ThemeFragment mThemeFragment = new ThemeFragment();
        return mThemeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(mView == null) mView = inflater.inflate(R.layout.fragment_theme, container, false);
        ButterKnife.bind(this, mView);
        mSwipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        autoRefresh();
        mCreateTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewThemeActivity.actionStart(getContext());
            }
        });
        mCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeCategoryListActivity.actionStart(getContext());
            }
        });
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchThemeActivity.actionStart(getContext(), 1);
            }
        });
        return mView;
    }

    @Override
    public void setPresenter(ThemeContract.Presenter presenter){
        mThemePresenter = presenter;
    }

    public void autoRefresh(){
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(true);
                mThemePresenter.start(mHotItem, mNewestItem);
                if(mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh(){
            mThemePresenter.start(mHotItem, mNewestItem);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void updateAdapter(List<Theme> themeList, int secondGroupPosition){
        mThemeList.clear();
        mThemeList.addAll(themeList);
        mSecondGroupPosition = secondGroupPosition;
        if(mThemeAdapter == null){
            mThemeAdapter = new ThemeAdapter(mThemeList, mSecondGroupPosition);
            mRecyclerView.setAdapter(mThemeAdapter);
        }else{
            mThemeAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showToast(String toast) {
        Toast.makeText(getContext(), toast, Toast.LENGTH_SHORT).show();
    }
}
