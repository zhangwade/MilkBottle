package com.wadezhang.milkbottle.post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.post_detail.PostDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PostFriendAdapter extends RecyclerView.Adapter<PostFriendAdapter.ViewHolder> {

    private Context mContext;

    private List<Post> mPostList;
    private boolean isLikes = false;

    public PostFriendAdapter(List<Post> postList){
        mPostList = postList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_post_viewpager_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Post post = mPostList.get(position);
                PostDetailActivity.actionStart(mContext, post, isLikes);
            }
        }); //TODO:isLikes要根据情况改变值
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mPostList != null){
            holder.mAddFollow.setVisibility(View.GONE);
            Post mPost = mPostList.get(position);
            holder.mTheme.setText(mPost.getTheme().getName());
            ImageLoader.with(mContext, mPost.getAuthor().getIcon().getFileUrl(), holder.mIcon);
            holder.mAuthor.setText(mPost.getAuthor().getUsername());
            if(mPost.getPhoto() != null) ImageLoader.with(mContext, mPost.getPhoto().getFileUrl(), holder.mPhoto);
            holder.mContent.setText(mPost.getContent());
            holder.mTime.setText(mPost.getCreatedAt());
            holder.mCommentCount.setText(mPost.getCommentCount().toString());
            holder.mLikesCount.setText(mPost.getLikesCount().toString());
        }
    }

    @Override
    public int getItemCount(){
        return mPostList == null ? 0 : mPostList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mPost;

        @BindView(R.id.fragment_post_viewpager_item_text_theme) TextView mTheme;
        @BindView(R.id.fragment_post_viewpager_item_img_icon) ImageView mIcon;
        @BindView(R.id.fragment_post_viewpager_item_text_author) TextView mAuthor;
        @BindView(R.id.fragment_post_viewpager_item_img_photo) ImageView mPhoto;
        @BindView(R.id.fragment_post_viewpager_item_text_content) TextView mContent;
        @BindView(R.id.fragment_post_viewpager_item_text_time) TextView mTime;
        @BindView(R.id.fragment_post_viewpager_item_text_comment_count) TextView mCommentCount;
        @BindView(R.id.fragment_post_viewpager_item_text_likes_count) TextView mLikesCount;
        @BindView(R.id.fragment_post_viewpager_item_text_follow) TextView mAddFollow;

        public ViewHolder(View view){
            super(view);
            mPost = view;
            ButterKnife.bind(this, view);
        }
    }
}
