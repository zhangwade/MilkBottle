package com.wadezhang.milkbottle.me;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class FollowOrFansAdapter extends RecyclerView.Adapter<FollowOrFansAdapter.ViewHolder> {

    private Context mContext;

    private List<User> mUserList;

    public FollowOrFansAdapter(List<User> postList){
        mUserList = postList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        View mView = LayoutInflater.from(mContext).inflate(R.layout.activity_follow_or_fans_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                User user = mUserList.get(position);
                //TODO
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        if(mUserList != null){
            User user = mUserList.get(position);
            ImageLoader.with(mContext, user.getIcon().getFileUrl(), holder.mIcon);
            holder.mNickname.setText(user.getNickname());
        }
    }

    @Override
    public int getItemCount(){
        return mUserList == null ? 0 : mUserList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        View mUser;

        @BindView(R.id.activity_follow_or_fans_item_img_icon) ImageView mIcon;
        @BindView(R.id.activity_follow_or_fans_item_text_nickname) TextView mNickname;

        public ViewHolder(View view){
            super(view);
            mUser = view;
            ButterKnife.bind(this, view);
        }
    }
}
