package com.wadezhang.milkbottle.post_detail;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class PostDetailCommentAdapter extends RecyclerView.Adapter<PostDetailCommentAdapter.ViewHolder> {

    private Context mContext;

    private List<Comment> mCommentList;

    public PostDetailCommentAdapter(List<Comment> commentList){
        mCommentList = commentList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_post_detail_comment_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Comment comment = mCommentList.get(position);
                WriteCommentActivity.actionStart(mContext, 1, comment.getPost().getObjectId(), comment.getAuthor().getObjectId(), comment.getAuthor().getNickname());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mCommentList != null){
            Comment mComment = mCommentList.get(position);
            ImageLoader.with(mContext, mComment.getAuthor().getIcon().getFileUrl(), holder.mAuthorIcon);
            holder.mAuthorName.setText(mComment.getAuthor().getNickname());
            holder.mTime.setText(mComment.getCreatedAt());
            if(mComment.getToWho() == null){
                holder.mContent.setText(mComment.getContent());
            }else{
                String toWhoNickname = mComment.getToWho().getNickname();
                holder.mContent.setText("回复 "+toWhoNickname+" : "+mComment.getContent());
                int length = toWhoNickname.length() + 6;
                Spannable spannable = new SpannableString(holder.mContent.getText());
                spannable.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.textColorSecond)), 0, length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.mContent.setText(spannable);
            }
            User user = GetCurrentUser.getCurrentUser(mContext);
            if(user == null) return;
            String mUserId = user.getObjectId();
            boolean isPostAuthor = ( mComment.getPost().getAuthor().getObjectId().equals(mUserId) );
            boolean isCommentAuthor = ( mComment.getAuthor().getObjectId().equals(mUserId) );
            if(!isPostAuthor && !isCommentAuthor) holder.mDelete.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount(){
        return mCommentList == null ? 0 : mCommentList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.activity_post_detail_comment_item_img_author_icon) ImageView mAuthorIcon;
        @BindView(R.id.activity_post_detail_comment_item_text_author_name) TextView mAuthorName;
        @BindView(R.id.activity_post_detail_comment_item_text_time) TextView mTime;
        @BindView(R.id.activity_post_detail_comment_item_text_content) TextView mContent;
        @BindView(R.id.activity_post_detail_comment_item_imgbtn_delete) TextView mDelete;

        View mComment;

        public ViewHolder(View view){
            super(view);
            ButterKnife.bind(this, view);
            mComment = view;
        }
    }
}
