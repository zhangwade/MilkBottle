package com.wadezhang.milkbottle.theme;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;

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

    private ThemeAdapter mThemeAdapter;
    private List<Theme> mPostList = new ArrayList<>();
    private final int HOT_ITEM = 6;  //取 6 条热门话题
    private final int NEWEST_ITEM = 30;  //取 30 条最新话题

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
        View mView = inflater.inflate(R.layout.fragment_theme, container, false);
        ButterKnife.bind(this, mView);
        mThemeAdapter = new ThemeAdapter(mPostList, HOT_ITEM + 1);
        mRecyclerView.setAdapter(mThemeAdapter);
        return mView;
    }

    @Override
    public void setPresenter(ThemeContract.Presenter presenter){
        mThemePresenter = presenter;
    }
}
