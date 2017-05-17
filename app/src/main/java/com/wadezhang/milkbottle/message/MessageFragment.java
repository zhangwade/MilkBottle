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
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post_detail.Comment;
import com.wadezhang.milkbottle.search.SearchUserActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListListener;

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

    private int commentItemHasMsg = 0;
    private int likesItemHasMsg = 0;
    private int fansItemHasMsg = 0;

    private List<Comment> commentUnReadList;
    private List<Likes> likesUnReadList;
    private List<Fans> fansUnReadList;

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
        User user = GetCurrentUser.getCurrentUser(getContext());
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
                    if(!list.isEmpty()){
                        mImgCommentTips.setVisibility(View.VISIBLE);
                        commentUnReadList = list;
                        commentItemHasMsg = 1;
                    }
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
                    if(!list.isEmpty()){
                        mImgLikesTips.setVisibility(View.VISIBLE);
                        likesUnReadList = list;
                        likesItemHasMsg = 1;
                    }
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
                    if(!list.isEmpty()){
                        mImgFansTips.setVisibility(View.VISIBLE);
                        fansUnReadList = list;
                        fansItemHasMsg = 1;
                    }
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
                SearchUserActivity.actionStart(getContext());
            }
        });
        mComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commentItemHasMsg == 1){
                    ThreadUpdateComment threadUpdateComment = new ThreadUpdateComment(commentUnReadList);
                    new Thread(threadUpdateComment).start();
                }
                commentItemHasMsg = 0;
                mImgCommentTips.setVisibility(View.INVISIBLE);
                MessageCommentActivity.actionStart(getContext());
            }
        });
        mLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(likesItemHasMsg == 1){
                    ThreadUpdateLikes threadUpdateLikes = new ThreadUpdateLikes(likesUnReadList);
                    new Thread(threadUpdateLikes).start();
                }
                likesItemHasMsg = 0;
                mImgLikesTips.setVisibility(View.INVISIBLE);
                MessageLikesActivity.actionStart(getContext());
            }
        });
        mFans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fansItemHasMsg == 1){
                    ThreadUpdateFans threadUpdateFans = new ThreadUpdateFans(fansUnReadList);
                    new Thread(threadUpdateFans).start();
                }
                fansItemHasMsg = 0;
                mImgFansTips.setVisibility(View.INVISIBLE);
                MessageFansActivity.actionStart(getContext());
            }
        });
        mWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mImgWechatTips.setVisibility(View.INVISIBLE);
            }
        });
    }

    public class ThreadUpdateComment implements Runnable{

        List<Comment> mList;
        int times = 1;

        public ThreadUpdateComment(List<Comment> list){
            mList = list;
        }

        @Override
        public void run(){
            if(mList.size() > 50) times = mList.size() / 50 + 1;
            for(int i = 0; i<times; i++){
                List<BmobObject> commentList = new ArrayList<>();
                for(int j = 0; j < 50; j++){
                    if(mList.size() != 0){
                        Comment comment = new Comment();
                        comment.setObjectId(mList.get(0).getObjectId());
                        comment.setIsRead(1);
                        commentList.add(comment);
                        mList.remove(0);
                    }else{
                        break;
                    }
                }
                new BmobBatch().updateBatch(commentList).doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {
                        if(e == null){

                        }else{
                            Toast.makeText(getContext(), "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "bmob更新comment失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        }
    }

    public class ThreadUpdateLikes implements Runnable{

        List<Likes> mList;
        int times = 1;

        public ThreadUpdateLikes(List<Likes> list){
            mList = list;
        }

        @Override
        public void run(){
            if(mList.size() > 50) times = mList.size() / 50 + 1;
            for(int i = 0; i<times; i++){
                List<BmobObject> likesList = new ArrayList<>();
                for(int j = 0; j < 50; j++){
                    if(mList.size() != 0){
                        Likes likes = new Likes();
                        likes.setObjectId(mList.get(0).getObjectId());
                        likes.setIsRead(1);
                        likesList.add(likes);
                        mList.remove(0);
                    }else{
                        break;
                    }
                }
                new BmobBatch().updateBatch(likesList).doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {
                        if(e == null){

                        }else{
                            Toast.makeText(getContext(), "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "bmob更新likes失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        }
    }

    public class ThreadUpdateFans implements Runnable{

        List<Fans> mList;
        int times = 1;

        public ThreadUpdateFans(List<Fans> list){
            mList = list;
        }

        @Override
        public void run(){
            if(mList.size() > 50) times = mList.size() / 50 + 1;
            for(int i = 0; i<times; i++){
                List<BmobObject> fansList = new ArrayList<>();
                for(int j = 0; j < 50; j++){
                    if(mList.size() != 0){
                        Fans fans = new Fans();
                        fans.setObjectId(mList.get(0).getObjectId());
                        fans.setIsRead(1);
                        fansList.add(fans);
                        mList.remove(0);
                    }else{
                        break;
                    }
                }
                new BmobBatch().updateBatch(fansList).doBatch(new QueryListListener<BatchResult>() {
                    @Override
                    public void done(List<BatchResult> list, BmobException e) {
                        if(e == null){

                        }else{
                            Toast.makeText(getContext(), "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                            Log.d(getClass().getSimpleName(), "bmob更新fans失败："+e.getMessage()+","+e.getErrorCode());
                        }
                    }
                });
            }
        }
    }
}
