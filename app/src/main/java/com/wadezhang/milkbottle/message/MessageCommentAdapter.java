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
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.me.UserDetailActivity;
import com.wadezhang.milkbottle.post_detail.Comment;
import com.wadezhang.milkbottle.post_detail.PostDetailActivity;
import com.wadezhang.milkbottle.watch_big_photo.WatchBigPhotoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/13 0013.
 */

public class MessageCommentAdapter extends RecyclerView.Adapter<MessageCommentAdapter.ViewHolder>{

    private Context mContext;

    private List<Comment> mCommentList;

    public MessageCommentAdapter(List<Comment> commentList){
        mCommentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_message_comment_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Comment comment = mCommentList.get(position);
                PostDetailActivity.actionStart(mContext, null, comment.getPost().getObjectId(), false);
            }
        });
        mViewHolder.mAuthorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Comment comment = mCommentList.get(position);
                UserDetailActivity.actionStart(mContext, comment.getAuthor().getObjectId());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mCommentList != null){
            Comment comment = mCommentList.get(position);
            ImageLoader.with(mContext, comment.getAuthor().getIcon().getFileUrl(), holder.mAuthorIcon);
            holder.mAuthorName.setText(comment.getAuthor().getNickname());
            holder.mContent.setText(comment.getContent());
            holder.mTime.setText(comment.getCreatedAt());
            if(comment.getPost().getPhoto() != null) ImageLoader.with(mContext, comment.getPost().getPhoto().getFileUrl(), holder.mPostPhoto);
        }
    }

    @Override
    public int getItemCount(){
        return mCommentList == null ? 0 : mCommentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mComment;

        @BindView(R.id.activity_message_comment_item_img_author_icon) ImageView mAuthorIcon;
        @BindView(R.id.activity_message_comment_item_text_author_name) TextView mAuthorName;
        @BindView(R.id.activity_message_comment_item_text_content) TextView mContent;
        @BindView(R.id.activity_message_comment_item_text_time) TextView mTime;
        @BindView(R.id.activity_message_comment_item_img_post_photo) ImageView mPostPhoto;

        public ViewHolder(View view){
            super(view);
            mComment = view;
            ButterKnife.bind(this, view);
        }
    }
}
