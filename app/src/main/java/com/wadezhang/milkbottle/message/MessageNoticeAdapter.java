package com.wadezhang.milkbottle.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.me.UserDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/22 0022.
 */

public class MessageNoticeAdapter extends RecyclerView.Adapter<MessageNoticeAdapter.ViewHolder> {

    private Context mContext;

    private List<Notice> mNoticeList;

    public MessageNoticeAdapter(List<Notice> noticeList){
        mNoticeList = noticeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_message_notice_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Notice notice = mNoticeList.get(position);
                MessageNoticeDetailActivity.actionStart(mContext, notice.getContent());
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mNoticeList != null){
            Notice notice = mNoticeList.get(position);
            ImageLoader.with(mContext, notice.getFrom().getIcon().getFileUrl(), holder.mFromIcon);
            holder.mFromName.setText(notice.getFrom().getNickname());
            holder.mTime.setText(notice.getCreatedAt());
            holder.mContent.setText(notice.getContent());
        }
    }

    @Override
    public int getItemCount(){
        return mNoticeList == null ? 0 : mNoticeList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mNotice;

        @BindView(R.id.activity_message_notice_item_img_icon) ImageView mFromIcon; //点赞人的头像
        @BindView(R.id.activity_message_notice_item_text_name) TextView mFromName;
        @BindView(R.id.activity_message_notice_item_text_time) TextView mTime;
        @BindView(R.id.activity_message_notice_item_text_content) TextView mContent;

        public ViewHolder(View view){
            super(view);
            mNotice = view;
            ButterKnife.bind(this, view);
        }
    }
}
