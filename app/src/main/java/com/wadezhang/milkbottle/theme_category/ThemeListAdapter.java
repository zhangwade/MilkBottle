package com.wadezhang.milkbottle.theme_category;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme.Theme;

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
        ViewHolder mViewHolder = new ViewHolder(mView);
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
