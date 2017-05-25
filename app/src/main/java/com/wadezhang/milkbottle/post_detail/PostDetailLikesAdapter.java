/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-17 上午8:19
 */

package com.wadezhang.milkbottle.post_detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.me.UserDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class PostDetailLikesAdapter extends RecyclerView.Adapter<PostDetailLikesAdapter.ViewHolder> {

    private Context mContext;

    private List<User> mLikesList;

    public PostDetailLikesAdapter(List<User> likesList){
        mLikesList = likesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_post_detail_likes_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                User user = mLikesList.get(position);
                UserDetailActivity.actionStart(mContext, user.getObjectId());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mLikesList != null){
            User user = mLikesList.get(position);
            ImageLoader.with(mContext, user.getIcon().getFileUrl(), holder.mIcon);
            holder.mNickname.setText(user.getNickname());
        }
    }

    @Override
    public int getItemCount(){
        return mLikesList == null ? 0 : mLikesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mUser;

        @BindView(R.id.activity_post_detail_likes_item_img_icon) ImageView mIcon;
        @BindView(R.id.activity_post_detail_likes_item_text_nickname) TextView mNickname;

        public ViewHolder(View view){
            super(view);
            mUser = view;
            ButterKnife.bind(this, view);
        }
    }
}
