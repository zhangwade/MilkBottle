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
import com.wadezhang.milkbottle.post_detail.PostDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/14 0014.
 */

public class MessageLikesAdapter extends RecyclerView.Adapter<MessageLikesAdapter.ViewHolder> {

    private Context mContext;

    private List<Likes> mLikesList;

    public MessageLikesAdapter(List<Likes> likesList){
        mLikesList = likesList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_message_likes_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Likes likes = mLikesList.get(position);
                PostDetailActivity.actionStart(mContext, null, likes.getPost().getObjectId(), false);
            }
        });
        mViewHolder.mAuthorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Likes likes = mLikesList.get(position);
                UserDetailActivity.actionStart(mContext, likes.getFrom().getObjectId());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mLikesList != null){
            Likes likes = mLikesList.get(position);
            ImageLoader.with(mContext, likes.getFrom().getIcon().getFileUrl(), holder.mAuthorIcon);
            holder.mAuthorName.setText(likes.getFrom().getNickname());
            holder.mTime.setText(likes.getCreatedAt());
            if(likes.getPost().getPhoto() != null) ImageLoader.with(mContext, likes.getPost().getPhoto().getFileUrl(), holder.mPostPhoto);
        }
    }

    @Override
    public int getItemCount(){
        return mLikesList == null ? 0 : mLikesList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mLikes;

        @BindView(R.id.activity_message_likes_item_img_author_icon) ImageView mAuthorIcon; //点赞人的头像
        @BindView(R.id.activity_message_likes_item_text_author_name) TextView mAuthorName;
        @BindView(R.id.activity_message_likes_item_text_time) TextView mTime;
        @BindView(R.id.activity_message_likes_item_img_post_photo) ImageView mPostPhoto;

        public ViewHolder(View view){
            super(view);
            mLikes = view;
            ButterKnife.bind(this, view);
        }
    }
}
