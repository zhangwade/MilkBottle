package com.wadezhang.milkbottle.theme_category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class ThemeCategoryListAdapter extends RecyclerView.Adapter<ThemeCategoryListAdapter.ViewHolder> {

    private Context mContext;

    private List<ThemeCategory> mThemeCategoryList;

    public ThemeCategoryListAdapter(List<ThemeCategory> themeCategoryList){
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
                ThemeListActivity.actionStart(mContext, themeCategory, 2);
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
