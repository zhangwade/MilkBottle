/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-16 下午10:39
 */

package com.wadezhang.milkbottle.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.me.UserDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/14 0014.
 */

public class MessageFansAdapter extends RecyclerView.Adapter<MessageFansAdapter.ViewHolder> {

    private Context mContext;

    private List<Fans> mFansList;

    public MessageFansAdapter(List<Fans> fansList){
        mFansList = fansList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_message_fans_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Fans fans = mFansList.get(position);
                UserDetailActivity.actionStart(mContext, fans.getFrom().getObjectId());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mFansList != null){
            Fans fans = mFansList.get(position);
            ImageLoader.with(mContext, fans.getFrom().getIcon().getFileUrl(), holder.mUserIcon);
            holder.mUserName.setText(fans.getFrom().getNickname());
            holder.mTime.setText(fans.getCreatedAt());
        }
    }

    @Override
    public int getItemCount(){
        return mFansList == null ? 0 : mFansList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mFans;

        @BindView(R.id.activity_message_fans_item_img_user_icon) ImageView mUserIcon;
        @BindView(R.id.activity_message_fans_item_text_user_name) TextView mUserName;
        @BindView(R.id.activity_message_fans_item_text_time) TextView mTime;

        public ViewHolder(View view){
            super(view);
            mFans = view;
            ButterKnife.bind(this, view);
        }
    }
}
