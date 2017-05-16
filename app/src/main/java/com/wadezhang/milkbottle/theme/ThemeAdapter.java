package com.wadezhang.milkbottle.theme;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.theme_category.ThemeListActivity;
import com.wadezhang.milkbottle.theme_post_list.ThemePostListActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public class ThemeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;

    private final int TYPE_GROUP_ITEM = 0;
    private final int TYPE_ITEM = 1;
    private int mSecondGroupPosition = 7; //默认第二个分组即“最新”分组是第 8 项

    private List<Theme> mThemeList;

    public ThemeAdapter(List<Theme> themeList, int secondGroupPosition){
        mSecondGroupPosition = secondGroupPosition;
        mThemeList = themeList;
    }

    @Override
    public int getItemViewType(int position){
        if(position == 0 || position == mSecondGroupPosition){
            return TYPE_GROUP_ITEM;
        }
        return TYPE_ITEM;
    }

    static class GroupItemViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.fragment_theme_group_item_text_title) TextView mGroupTitle;
        @BindView(R.id.fragment_theme_group_item_text_more) TextView mBtnMore;

        public GroupItemViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder{

        View mTheme;
        @BindView(R.id.fragment_theme_item_text_name) TextView mThemeName;
        @BindView(R.id.fragment_theme_item_text_postCount) TextView mPostCount;

        public ItemViewHolder(View view){
            super(view);
            mTheme = view;
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View view;
        if(viewType == TYPE_GROUP_ITEM){
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_theme_group_item, parent, false);
            final GroupItemViewHolder mGroupItemViewHolder = new GroupItemViewHolder(view);
            mGroupItemViewHolder.mBtnMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mGroupItemViewHolder.getLayoutPosition();
                    //Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show();
                    ThemeListActivity.actionStart(mContext, null, (position == 0 ? 0 : 1), null);
                }
            });
            return mGroupItemViewHolder;
        }else if(viewType == TYPE_ITEM){
            view = LayoutInflater.from(mContext).inflate(R.layout.fragment_theme_item, parent, false);
            final ItemViewHolder mItemViewHolder = new ItemViewHolder(view);
            mItemViewHolder.mTheme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = mItemViewHolder.getAdapterPosition();
                    Theme theme = new Theme();
                    if(position < mSecondGroupPosition){
                        theme = mThemeList.get(position - 1);
                    }else if(position > mSecondGroupPosition){
                        theme = mThemeList.get(position - 2);
                    }
                    ThemePostListActivity.actionStart(mContext, theme);
                    //Toast.makeText(mContext, Integer.toString(position), Toast.LENGTH_SHORT).show(); //TODO
                }
            });
            return mItemViewHolder;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position){
        if(mThemeList.isEmpty()) return;
        if(holder instanceof  GroupItemViewHolder){
            if(position == 0) ((GroupItemViewHolder) holder).mGroupTitle.setText("热门话题");
                else ((GroupItemViewHolder) holder).mGroupTitle.setText("最近更新的话题");
        }else if(holder instanceof ItemViewHolder){
            Theme theme = null;
            if(position < mSecondGroupPosition){
                theme = mThemeList.get(position - 1);
            }else if(position > mSecondGroupPosition){
                theme = mThemeList.get(position - 2);
            }
            ((ItemViewHolder) holder).mThemeName.setText(theme.getName());
            ((ItemViewHolder) holder).mPostCount.setText(Integer.toString(theme.getPostCount())+" 个帖子");
        }
    }

    @Override
    public int getItemCount(){
        return mThemeList == null ? 2 : mThemeList.size() + 2;
    }
}