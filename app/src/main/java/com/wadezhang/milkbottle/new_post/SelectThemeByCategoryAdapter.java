/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-15 下午8:34
 */

package com.wadezhang.milkbottle.new_post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme_category.ThemeCategory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class SelectThemeByCategoryAdapter extends RecyclerView.Adapter<SelectThemeByCategoryAdapter.ViewHolder> {

    private Context mContext;

    private List<ThemeCategory> mThemeCategoryList;

    public SelectThemeByCategoryAdapter(List<ThemeCategory> themeCategoryList){
        mThemeCategoryList = themeCategoryList;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.activity_theme_category_list_recyclerview_item_text_title) TextView mTitle;
        @BindView(R.id.activity_theme_category_list_recyclerview_item_text_subtitle) TextView mSubTitle;

        View mThemeCategoryView;

        public ViewHolder(View view){
            super(view);
            mThemeCategoryView = view;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_theme_category_list_recyclerview_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mThemeCategoryView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                ThemeCategory themeCategory = mThemeCategoryList.get(position);
                SelectThemeActivity.actionStart(mContext, themeCategory);
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (!mThemeCategoryList.isEmpty()) {
            ThemeCategory mThemeCategory = mThemeCategoryList.get(position);
            holder.mTitle.setText(mThemeCategory.getTitle());
            holder.mSubTitle.setText(mThemeCategory.getSubTitle());
        }
    }

    @Override
    public int getItemCount(){
        return mThemeCategoryList == null ? 0 : mThemeCategoryList.size();
    }
}
