/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-8 上午12:25
 */

package com.wadezhang.milkbottle.theme_category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme.Theme;
import com.wadezhang.milkbottle.theme_post_list.ThemePostListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/6 0006.
 */

public class ThemeListAdapter extends RecyclerView.Adapter<ThemeListAdapter.ViewHolder>{

    private Context mContext;

    private List<Theme> mThemeList;

    public ThemeListAdapter(List<Theme> themeList){
        mThemeList = themeList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mTheme;

        @BindView(R.id.fragment_theme_item_text_name) TextView mThemeName;
        @BindView(R.id.fragment_theme_item_text_postCount) TextView mThemePostCount;

        public ViewHolder(View view){
            super(view);
            mTheme = view;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_theme_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Theme theme = mThemeList.get(position);
                ThemePostListActivity.actionStart(mContext, theme);
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!mThemeList.isEmpty()) {
            Theme mTheme = mThemeList.get(position);
            holder.mThemeName.setText(mTheme.getName());
            holder.mThemePostCount.setText(mTheme.getPostCount().toString()+" 个帖子");
        }
    }

    @Override
    public int getItemCount(){
        return mThemeList == null ? 0 : mThemeList.size();
    }
}
