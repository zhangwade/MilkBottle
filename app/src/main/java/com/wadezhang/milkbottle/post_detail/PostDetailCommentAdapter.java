/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-22 下午5:18
 */

package com.wadezhang.milkbottle.post_detail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.me.UserDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class PostDetailCommentAdapter extends RecyclerView.Adapter<PostDetailCommentAdapter.ViewHolder> {

    private Context mContext;

    private String administratorId = "jDMNRNNR";

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
        mViewHolder.mAuthorIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Comment comment = mCommentList.get(position);
                UserDetailActivity.actionStart(mContext, comment.getAuthor().getObjectId());
            }
        });
        mViewHolder.mDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Comment comment = mCommentList.get(position);
                final String commentId = comment.getObjectId();
                final String authorObjectId = comment.getAuthor().getObjectId();
                final String commentContent = comment.getContent();

                final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("正在删除...");
                mProgressDialog.setCancelable(false);

                AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                mAlertDialog.setTitle("确定删除评论?");
                mAlertDialog.setMessage("删除后无法恢复");
                mAlertDialog.setCancelable(true);
                mAlertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mProgressDialog.show();
                        Comment comment = new Comment();
                        comment.setObjectId(commentId);
                        comment.delete(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                mProgressDialog.dismiss();
                                if(e == null){
                                    User me = GetCurrentUser.getCurrentUser(mContext);
                                    if (me.getObjectId().equals(administratorId)){
                                        Intent intent = new Intent(mContext, CreateNoticeService.class);
                                        intent.putExtra("type", 1);
                                        intent.putExtra("userId", authorObjectId);
                                        intent.putExtra("content", commentContent);
                                        mContext.startService(intent);
                                    }
                                    Intent mIntent = new Intent("com.wadezhang.milkbottle.REFRESH_COMMENT_LIST");
                                    mContext.sendBroadcast(mIntent);
                                }else{
                                    Toast.makeText(mContext, "删除失败! 请检查网络是否开启", Toast.LENGTH_SHORT).show();
                                    Log.d(getClass().getSimpleName(), "bmob删除评论失败："+e.getMessage()+","+e.getErrorCode());
                                }
                            }
                        });
                    }
                });
                mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                mAlertDialog.show();
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
            boolean isAdministrator = ( mUserId.equals(administratorId) );
            if(!isAdministrator && !isPostAuthor && !isCommentAuthor) holder.mDelete.setVisibility(View.INVISIBLE);
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
