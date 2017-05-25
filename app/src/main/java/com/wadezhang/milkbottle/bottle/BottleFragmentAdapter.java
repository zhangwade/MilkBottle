/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-13 上午11:13
 */

package com.wadezhang.milkbottle.bottle;

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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class BottleFragmentAdapter extends RecyclerView.Adapter<BottleFragmentAdapter.ViewHolder> {

    private Context mContext;

    private List<Essay> mEssayList;

    public BottleFragmentAdapter(List<Essay> essayList){
        mEssayList = essayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_bottle_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mEssay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Essay essay = mEssayList.get(position);
                EssayDetailActivity.actionStart(mContext, essay);
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mEssayList != null){
            Essay essay = mEssayList.get(position);
            ImageLoader.with(mContext, essay.getPhoto().getFileUrl(), holder.mPhoto);
            holder.mTitle.setText(essay.getTitle());
            holder.mAuthor.setText(essay.getAuthor());
        }
    }

    @Override
    public int getItemCount(){
        return mEssayList == null ? 0 : mEssayList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mEssay;

        @BindView(R.id.fragment_bottle_item_img_photo) ImageView mPhoto;
        @BindView(R.id.fragment_bottle_item_text_title) TextView mTitle;
        @BindView(R.id.fragment_bottle_item_text_author) TextView mAuthor;

        public ViewHolder(View view){
            super(view);
            mEssay = view;
            ButterKnife.bind(this, view);
        }
    }
}
