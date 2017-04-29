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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PostFriendAdapter extends RecyclerView.Adapter<PostFriendAdapter.ViewHolder> {

    private Context mContext;

    private List<Post> mPostList;

    public PostFriendAdapter(List<Post> postList){
        mPostList = postList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_post_viewpager_item, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mPostList != null){//TODO: 缺了几个控件的赋值
            Post mPost = mPostList.get(position);
            holder.mTheme.setText(mPost.getTheme().getName());
            ImageLoader.with(mContext, mPost.getAuthor().getIcon().getFileUrl(), holder.mIcon);
            holder.mAuthor.setText(mPost.getAuthor().getUsername());
            ImageLoader.with(mContext, mPost.getPhoto().getFileUrl(), holder.mPhoto);
            holder.mContent.setText(mPost.getContent());
            holder.mTime.setText(mPost.getCreatedAt());
        }
    }

    @Override
    public int getItemCount(){
        return mPostList == null ? 0 : mPostList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.fragment_post_viewpager_item_text_recommender) TextView mRecommender;
        @BindView(R.id.fragment_post_viewpager_item_text_theme) TextView mTheme;
        @BindView(R.id.fragment_post_viewpager_item_img_icon) ImageView mIcon;
        @BindView(R.id.fragment_post_viewpager_item_text_author) TextView mAuthor;
        @BindView(R.id.fragment_post_viewpager_item_img_photo) ImageView mPhoto;
        @BindView(R.id.fragment_post_viewpager_item_text_content) TextView mContent;
        @BindView(R.id.fragment_post_viewpager_item_text_time) TextView mTime;
        @BindView(R.id.fragment_post_viewpager_item_text_recommend_count) TextView mRecommendCount;
        @BindView(R.id.fragment_post_viewpager_item_text_comment_count) TextView mCommentCount;
        @BindView(R.id.fragment_post_viewpager_item_text_good_count) TextView mGoodCount;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
