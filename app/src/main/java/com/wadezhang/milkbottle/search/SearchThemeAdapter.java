package com.wadezhang.milkbottle.search;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.new_post.NewPostActivity;
import com.wadezhang.milkbottle.theme.Theme;
import com.wadezhang.milkbottle.theme_post_list.ThemePostListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/17 0017.
 */

public class SearchThemeAdapter extends RecyclerView.Adapter<SearchThemeAdapter.ViewHolder> {

    private Context mContext;

    private List<Theme> mThemeList;

    private int mType;
    private final int TYPE_NEW_POST = 0;
    private final int TYPE_SEARCH_THEME = 1;

    public SearchThemeAdapter(List<Theme> themeList, int type){
        mThemeList = themeList;
        mType = type;
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
                if(mType == TYPE_NEW_POST){
                    Intent intent = new Intent("com.wadezhang.milkbottle.NEW_POST_SELECT_THEME");
                    intent.putExtra("themeId", theme.getObjectId());
                    intent.putExtra("themeName", theme.getName());
                    mContext.sendBroadcast(intent);
                    Intent mIntent = new Intent(mContext, NewPostActivity.class);
                    mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    mContext.startActivity(mIntent);
                }else{
                    ThemePostListActivity.actionStart(mContext, theme);
                }
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
