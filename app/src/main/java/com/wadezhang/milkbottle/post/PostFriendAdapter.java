package com.wadezhang.milkbottle.post;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.me.UserDetailActivity;
import com.wadezhang.milkbottle.message.Likes;
import com.wadezhang.milkbottle.post_detail.PostDetailActivity;
import com.wadezhang.milkbottle.theme_post_list.ThemeDetailActivity;
import com.wadezhang.milkbottle.watch_big_photo.WatchBigPhotoActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PostFriendAdapter extends RecyclerView.Adapter<PostFriendAdapter.ViewHolder> {

    private Context mContext;

    private List<Post> mPostList;

    private final int TYPE_MY_POST = 0;
    private final int TYPE_OTHER = 1;
    private int mType;

    private final int STATE_NON_LIKES = 0; //初始状态：没点赞
    private final int STATE_LIKES = 1;  //初始状态：已点赞
    private final int STATE_NON_LIKES_CLICK = 2; //点击点赞按钮，先判断状态为 没点赞
    private final int STATE_LIKES_CLICK = 3;  //点击点赞按钮，先判断状态为 已点赞

    private Handler handler;

    public PostFriendAdapter(List<Post> postList, int type){
        mPostList = postList;
        mType = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if(mContext == null) mContext = parent.getContext();
        final View mView = LayoutInflater.from(mContext).inflate(R.layout.fragment_post_viewpager_item, parent, false);
        final ViewHolder mViewHolder = new ViewHolder(mView);
        mViewHolder.mPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Post post = mPostList.get(position);
                PostDetailActivity.actionStart(mContext, post, null);
            }
        });
        mViewHolder.mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Post post = mPostList.get(position);
                String photoUrl = post.getPhoto().getFileUrl();
                WatchBigPhotoActivity.actionStart(mContext, photoUrl);
            }
        });
        mViewHolder.mTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Post post = mPostList.get(position);
                ThemeDetailActivity.actionStart(mContext, post.getTheme().getObjectId());
            }
        });
        mViewHolder.mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                Post post = mPostList.get(position);
                UserDetailActivity.actionStart(mContext, post.getAuthor().getObjectId());
            }
        });
        mViewHolder.mImgBtnLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = mViewHolder.getAdapterPosition();
                final Post post = mPostList.get(position);
                checkLikes(post.getObjectId(), 1);
                handler = new Handler(){
                    public void handleMessage(Message msg){
                        switch (msg.what){
                            case STATE_LIKES_CLICK :
                                mViewHolder.mImgBtnLikes.setBackgroundResource(R.mipmap.non_likes);
                                cancelLikes(post.getObjectId(), post.getAuthor().getObjectId());
                                break;
                            case STATE_NON_LIKES_CLICK :
                                mViewHolder.mImgBtnLikes.setBackgroundResource(R.mipmap.likes);
                                addLikes(post.getObjectId(), post.getAuthor().getObjectId());
                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        });
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){  //TODO: final
        if(mPostList != null){
            holder.mAddFollow.setVisibility(View.GONE);
            Post mPost = mPostList.get(position);
            holder.mTheme.setText(mPost.getTheme().getName());
            ImageLoader.with(mContext, mPost.getAuthor().getIcon().getFileUrl(), holder.mIcon);
            if(mType == TYPE_MY_POST) holder.mIcon.setEnabled(false);
            holder.mAuthor.setText(mPost.getAuthor().getNickname());
            if(mPost.getPhoto() != null)
                ImageLoader.with(mContext, mPost.getPhoto().getFileUrl(), holder.mPhoto);
            else
                holder.mPhoto.setVisibility(View.GONE);
            holder.mContent.setText(mPost.getContent());
            holder.mTime.setText(mPost.getCreatedAt());
            holder.mCommentCount.setText(mPost.getCommentCount().toString());
            checkLikes(mPost.getObjectId(), 0);
            handler = new Handler(){
                public void handleMessage(Message msg){
                    switch (msg.what){
                        case STATE_LIKES :
                            holder.mImgBtnLikes.setBackgroundResource(R.mipmap.likes);
                            break;
                        default:
                            break;
                    }
                }
            };
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
        @BindView(R.id.fragment_post_viewpager_item_text_follow) TextView mAddFollow;
        @BindView(R.id.fragment_post_viewpager_item_imgbtn_likes) ImageButton mImgBtnLikes;

        public ViewHolder(View view){
            super(view);
            mPost = view;
            ButterKnife.bind(this, view);
        }
    }

    public void checkLikes(String postId, final int tag){
        User user = GetCurrentUser.getCurrentUser(mContext);
        if(user == null) return;
        BmobQuery<Likes> query = new BmobQuery<>();
        User mUser = new User();
        mUser.setObjectId(user.getObjectId());
        Post post = new Post();
        post.setObjectId(postId);
        query.addWhereEqualTo("from", mUser);
        query.addWhereEqualTo("post", post);
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<Likes>() {
            @Override
            public void done(List<Likes> list, BmobException e) {
                if(e == null){
                    Message message = new Message();
                    if(!list.isEmpty()){
                        if(tag == 0) message.what = STATE_LIKES;
                            else message.what = STATE_LIKES_CLICK;
                    }else{
                        if(tag == 0) message.what = STATE_NON_LIKES;
                            else message.what = STATE_NON_LIKES_CLICK;
                    }
                    handler.sendMessage(message);
                }
            }
        });
    }

    public void addLikes(String postId, String postAuthorId){
        User user = GetCurrentUser.getCurrentUser(mContext);
        if(user == null)return;
        Post post = new Post();
        post.setObjectId(postId);
        BmobRelation relation = new BmobRelation();
        relation.add(user);
        post.setLikes(relation);
        post.increment("likesCount");
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
        Likes likes = new Likes();
        likes.setIsRead(0);
        likes.setFrom(user);
        likes.setPost(post);
        User postAuthor = new User();
        postAuthor.setObjectId(postAuthorId);
        likes.setTo(postAuthor);
        likes.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

            }
        });
    }

    public void cancelLikes(String postId, String postAuthorId){
        User user = GetCurrentUser.getCurrentUser(mContext);
        Post post = new Post();
        post.setObjectId(postId);
        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        post.setLikes(relation);
        post.increment("likesCount",-1);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {

            }
        });
        BmobQuery<Likes> query = new BmobQuery<>();
        query.addWhereEqualTo("from", user);
        query.addWhereEqualTo("post", post);
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<Likes>() {
            @Override
            public void done(List<Likes> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    Likes likes = new Likes();
                    likes.setObjectId(list.get(0).getObjectId());
                    likes.delete(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {

                        }
                    });
                }
            }
        });
    }
}
