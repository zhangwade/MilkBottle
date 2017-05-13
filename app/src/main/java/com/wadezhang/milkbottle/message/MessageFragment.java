package com.wadezhang.milkbottle.message;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseFragment;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post_detail.Comment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by zhangxix on 2017/3/6.
 */

public class MessageFragment extends BaseFragment {

    @BindView(R.id.fragment_message_text_search_user) TextView mSearch;
    @BindView(R.id.fragment_message_swiperefreshlayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.fragment_message_comment_item) LinearLayout mComment;
    @BindView(R.id.fragment_message_likes_item) LinearLayout mLikes;
    @BindView(R.id.fragment_message_fans_item) LinearLayout mFans;
    @BindView(R.id.fragment_message_wechat_item) LinearLayout mWechat;
    @BindView(R.id.fragment_message_img_comment_tips) ImageView mImgCommentTips;
    @BindView(R.id.fragment_message_img_likes_tips) ImageView mImgLikesTips;
    @BindView(R.id.fragment_message_img_fans_tips) ImageView mImgFansTips;
    @BindView(R.id.fragment_message_img_wechat_tips) ImageView mImgWechatTips;

    private final int READ_NO = 0;
    private final int READ_YES = 1;

    public static MessageFragment newInstance() {
        MessageFragment mMessageFragment = new MessageFragment();
        return mMessageFragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_message, container, false);
        ButterKnife.bind(this, mView);
        check();
        onClick();
        mSwipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        return mView;
    }

    public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener{
        @Override
        public void onRefresh(){
            check();
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public void check(){
        User user = BmobUser.getCurrentUser(User.class);
        if(user == null) return;
        User mUser = new User();
        mUser.setObjectId(user.getObjectId());
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.addWhereEqualTo("toWho", mUser);
        commentBmobQuery.addWhereEqualTo("isRead", READ_NO);
        commentBmobQuery.addQueryKeys("objectId");
        commentBmobQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()) mImgCommentTips.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        BmobQuery<Likes> likesBmobQuery = new BmobQuery<>();
        likesBmobQuery.addWhereEqualTo("to", mUser);
        likesBmobQuery.addWhereEqualTo("isRead", READ_NO);
        likesBmobQuery.addQueryKeys("objectId");
        likesBmobQuery.findObjects(new FindListener<Likes>() {
            @Override
            public void done(List<Likes> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()) mImgLikesTips.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        BmobQuery<Fans> fansBmobQuery = new BmobQuery<>();
        fansBmobQuery.addWhereEqualTo("to", mUser);
        fansBmobQuery.addWhereEqualTo("isRead", READ_NO);
        fansBmobQuery.addQueryKeys("objectId");
        fansBmobQuery.findObjects(new FindListener<Fans>() {
            @Override
            public void done(List<Fans> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()) mImgFansTips.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        BmobQuery<Wechat> wechatBmobQuery = new BmobQuery<>();
        wechatBmobQuery.addWhereEqualTo("to", mUser);
        wechatBmobQuery.addWhereEqualTo("isRead", READ_NO);
        wechatBmobQuery.addQueryKeys("objectId");
        wechatBmobQuery.findObjects(new FindListener<Wechat>() {
            @Override
            public void done(List<Wechat> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()) mImgWechatTips.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(getContext(), "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void onClick(){
        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgCommentTips.setVisibility(View.INVISIBLE);
            }
        });
        mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgLikesTips.setVisibility(View.INVISIBLE);
            }
        });
        mFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgFansTips.setVisibility(View.INVISIBLE);
            }
        });
        mWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgWechatTips.setVisibility(View.INVISIBLE);
            }
        });
    }
}
