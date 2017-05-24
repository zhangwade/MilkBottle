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
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.post_detail.Comment;
import com.wadezhang.milkbottle.search.SearchUserActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
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
    @BindView(R.id.fragment_message_notice_item) LinearLayout mNotice;
    @BindView(R.id.fragment_message_img_comment_tips) ImageView mImgCommentTips;
    @BindView(R.id.fragment_message_img_likes_tips) ImageView mImgLikesTips;
    @BindView(R.id.fragment_message_img_fans_tips) ImageView mImgFansTips;
    @BindView(R.id.fragment_message_img_notice_tips) ImageView mImgNoticeTips;

    private final int READ_NO = 0;
    private final int READ_YES = 1;

    private int commentItemHasMsg = 0;
    private int likesItemHasMsg = 0;
    private int fansItemHasMsg = 0;
    private int noticeItemHasMsg = 0;

    private List<Comment> commentUnReadList;
    private List<Likes> likesUnReadList;
    private List<Fans> fansUnReadList;
    private List<Notice> noticeUnReadList;

    private List<Post> myPostList = new ArrayList<>();; //我的所有帖子的 Id
    User mUser;

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
        mUser = new User();
        mUser.setObjectId(user.getObjectId());

        BmobQuery<Post> mMyPostQuery = new BmobQuery<>();  //先查我的 帖子 列表
        mMyPostQuery.addWhereEqualTo("author", mUser);
        mMyPostQuery.addQueryKeys("objectId");
        mMyPostQuery.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) { //查询当前用户的关注
                if(e == null){
                    myPostList.clear();
                    if(!list.isEmpty()){
                        myPostList = list;
                    }
                    BmobQuery<Comment> q1 = new BmobQuery<>();  //再查评论消息
                    q1.addWhereEqualTo("isRead", READ_NO);

                    BmobQuery<Comment> q3 = new BmobQuery<>();
                    q3.addWhereEqualTo("toWho", mUser);
                    List<BmobQuery<Comment>> queries = new ArrayList<BmobQuery<Comment>>();

                    queries.add(q3);
                    for (Post post : myPostList){
                        BmobQuery<Comment> q = new BmobQuery<>();
                        q.addWhereEqualTo("post", post);
                        queries.add(q);
                    }
                    BmobQuery<Comment> mainQuery = new BmobQuery<>();
                    BmobQuery<Comment> or = mainQuery.or(queries);

                    List<BmobQuery<Comment>> andQuery = new ArrayList<>();
                    andQuery.add(q1);
                    andQuery.add(or);

                    BmobQuery<Comment> query = new BmobQuery<>();
                    query.and(andQuery);
                    query.addQueryKeys("objectId");
                    query.findObjects(new FindListener<Comment>() {
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
                }else{
                    Log.d(getClass().getSimpleName(), "bmob查询myPostIds失败："+e.getMessage()+","+e.getErrorCode());
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
        BmobQuery<Notice> noticeBmobQuery = new BmobQuery<>();
        noticeBmobQuery.addWhereEqualTo("to", mUser);
        noticeBmobQuery.addWhereEqualTo("isRead", READ_NO);
        noticeBmobQuery.addQueryKeys("objectId");
        noticeBmobQuery.findObjects(new FindListener<Notice>() {
            @Override
            public void done(List<Notice> list, BmobException e) {
                if(e == null){
                    if(!list.isEmpty()) mImgNoticeTips.setVisibility(View.VISIBLE);
                    noticeUnReadList = list;
                    noticeItemHasMsg = 1;
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
        mNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(noticeItemHasMsg == 1){
                    ThreadUpdateNotice threadUpdateNotice = new ThreadUpdateNotice(noticeUnReadList);
                    new Thread(threadUpdateNotice).start();
                }
                noticeItemHasMsg = 0;
                mImgNoticeTips.setVisibility(View.INVISIBLE);
                MessageNoticeActivity.actionStart(getContext());
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

    public class ThreadUpdateNotice implements Runnable{

        List<Notice> mList;
        int times = 1;

        public ThreadUpdateNotice(List<Notice> list){
            mList = list;
        }

        @Override
        public void run(){
            if(mList.size() > 50) times = mList.size() / 50 + 1;
            for(int i = 0; i<times; i++){
                List<BmobObject> noticeList = new ArrayList<>();
                for(int j = 0; j < 50; j++){
                    if(mList.size() != 0){
                        Notice notice = new Notice();
                        notice.setObjectId(mList.get(0).getObjectId());
                        notice.setIsRead(1);
                        noticeList.add(notice);
                        mList.remove(0);
                    }else{
                        break;
                    }
                }
                new BmobBatch().updateBatch(noticeList).doBatch(new QueryListListener<BatchResult>() {
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
