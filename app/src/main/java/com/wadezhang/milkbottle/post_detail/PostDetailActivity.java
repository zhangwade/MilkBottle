package com.wadezhang.milkbottle.post_detail;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.SwipyAppBarScrollListener;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.new_post.NewPostActivity;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.theme.Theme;
import com.wadezhang.milkbottle.theme_post_list.ThemePostListActivity;
import com.wadezhang.milkbottle.watch_big_photo.WatchBigPhotoActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class PostDetailActivity extends BaseActivity {

    @BindView(R.id.activity_post_detail_swiperefreshlayout) SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.activity_post_detail_imgbtn_back) ImageButton mButtonBack;
    @BindView(R.id.activity_post_detail_text_theme) TextView mTheme;
    @BindView(R.id.activity_post_detail_img_author_icon) ImageView mAuthorIcon;
    @BindView(R.id.activity_post_detail_text_author_name) TextView mAuthorName;
    @BindView(R.id.activity_post_detail_img_photo) ImageView mPhoto;
    @BindView(R.id.activity_post_detail_text_content) TextView mContent;
    @BindView(R.id.activity_post_detail_relativelayout_likes) RelativeLayout mLikesitem;
    @BindView(R.id.activity_post_detail_text_likes_count) TextView mLikesCount;
    @BindView(R.id.activity_post_detail_text_comment_count) TextView mCommentCount;
    @BindView(R.id.activity_post_detail_comment_recyclerview) RecyclerView mCommentRecyclerView;
    @BindView(R.id.activity_post_detail_imgbtn_likes) ImageButton mLikes; //TODO
    @BindView(R.id.activity_post_detail_text_write_comment) TextView mWriteComment;

    Context mContext;

    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 10; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多

    private String postObjectId;
    private String themeObjectId;
    private String themeName;
    private String authorObjectId;
    private String photoUrl;

    private PostDetailCommentAdapter mPostDetailCommentAdapter;
    private List<Comment> mCommentList = new ArrayList<>();

    private IntentFilter intentFilter;
    private SendCommentReceiver sendCommentReceiver;

    public static void actionStart(Context context, Post post, String postId, boolean isLikes){
        Intent mIntent = new Intent(context, PostDetailActivity.class);
        if(post != null){
            mIntent.putExtra("postObjectId", post.getObjectId());
            mIntent.putExtra("themeObjectId", post.getTheme().getObjectId());
            mIntent.putExtra("themeName", post.getTheme().getName());
            mIntent.putExtra("authorObjectId", post.getAuthor().getObjectId());
            mIntent.putExtra("authorIcon", post.getAuthor().getIcon().getFileUrl());
            mIntent.putExtra("authorName", post.getAuthor().getUsername());
            if(post.getPhoto() != null) mIntent.putExtra("photo", post.getPhoto().getFileUrl());
            mIntent.putExtra("content", post.getContent());
            mIntent.putExtra("isLikes", isLikes); //TODO:点赞的图案切换
        }else{
            mIntent.putExtra("postId", postId);
        }
        context.startActivity(mIntent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
        mContext = this;
        initPost();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mCommentRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostDetailCommentAdapter = new PostDetailCommentAdapter(mCommentList);
        mCommentRecyclerView.setAdapter(mPostDetailCommentAdapter);
        mCommentRecyclerView.addOnScrollListener(new LoadMoreListener());
        getComment(STATE_REFRESH);
        mSwipeRefreshLayout.setOnRefreshListener(new RefreshListener());
        intentFilter = new IntentFilter();
        intentFilter.addAction("com.wadezhang.milkbottle.SEND_COMMENT_SUCCESS");
        sendCommentReceiver = new SendCommentReceiver();
        registerReceiver(sendCommentReceiver, intentFilter);
        onClick();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(sendCommentReceiver);
    }

    public class RefreshListener implements SwipeRefreshLayout.OnRefreshListener {

        @Override
        public void onRefresh(){
            getComment(STATE_REFRESH);
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener extends RecyclerView.OnScrollListener{
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //Log.d("------->isSlideToBottom:" + isSlideToBottom(recyclerView));
            if (isSlideToBottom(recyclerView)) {
                //srlLayout.setEnabled(true);
                mSwipeRefreshLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(true);
                        getComment(STATE_MORE);
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    public void initPost(){
        Intent mIntent = getIntent();
        if(mIntent.getStringExtra("postObjectId") != null){
            postObjectId = mIntent.getStringExtra("postObjectId");
            themeObjectId = mIntent.getStringExtra("themeObjectId");
            authorObjectId = mIntent.getStringExtra("authorObjectId");
            themeName = mIntent.getStringExtra("themeName");
            mTheme.setText(themeName);
            ImageLoader.with(this, mIntent.getStringExtra("authorIcon"), mAuthorIcon);
            mAuthorName.setText(mIntent.getStringExtra("authorName"));
            if(mIntent.getStringExtra("photo") != null){
                photoUrl = mIntent.getStringExtra("photo");
                ImageLoader.with(this, photoUrl, mPhoto);
            } else{
                mPhoto.setVisibility(View.GONE);
            }
            mContent.setText(mIntent.getStringExtra("content"));
            //mLikes  TODO
        }else{
            postObjectId = mIntent.getStringExtra("postId");
            BmobQuery<Post> query = new BmobQuery<>();
            query.addQueryKeys("theme,author,photo,content");
            query.include("theme[objectId|name],author[objectId|icon|nickname]");
            query.getObject(postObjectId, new QueryListener<Post>() {
                @Override
                public void done(Post post, BmobException e) {
                    if(e == null){
                        themeObjectId = post.getTheme().getObjectId();
                        authorObjectId = post.getAuthor().getObjectId();
                        themeName = post.getTheme().getName();
                        mTheme.setText(themeName);
                        ImageLoader.with(mContext, post.getAuthor().getIcon().getFileUrl(), mAuthorIcon);
                        mAuthorName.setText(post.getAuthor().getNickname());
                        if(post.getPhoto() != null){
                            photoUrl = post.getPhoto().getFileUrl();
                            ImageLoader.with(mContext, photoUrl, mPhoto);
                        } else{
                            mPhoto.setVisibility(View.GONE);
                        }
                        mContent.setText(post.getContent());
                        //mLikes  TODO
                    }
                }
            });
        }
    }

    public void getComment(int actionType){
        mActionType = actionType;
        BmobQuery<Comment> mCommentQuery = new BmobQuery<>();
        Post mPost = new Post();
        mPost.setObjectId(postObjectId);
        mCommentQuery.addWhereEqualTo("post", mPost);
        mCommentQuery.order("-createdAt");
        mCommentQuery.addQueryKeys("objectId,post,author,createdAt,content,toWho");
        mCommentQuery.include("post[objectId].author[objectId],author[objectId|nickname|icon],toWho[nickname]");
        if (mActionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                //Log.i("0414", date.toString());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            mCommentQuery.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            mCommentQuery.setSkip(curPage * limit - 1);
        }
        // 设置每页数据个数
        mCommentQuery.setLimit(limit);
        // 查找数据
        mCommentQuery.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> list, BmobException e) {
                if(e == null){
                    if (!list.isEmpty()) {
                        if (mActionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            // 获取目前最新的时间
                            lastTime = list.get(0).getCreatedAt();
                        }
                        // 这里在每次加载完数据后，将当前页码+1
                        curPage++;
                        showCommentList(mActionType, list);
                    } else if (mActionType == STATE_MORE) {
                        Toast.makeText(mContext, "到底了", Toast.LENGTH_SHORT).show();
                    } else if (mActionType == STATE_REFRESH) {
                        Toast.makeText(mContext, "来发表第一条评论吧~~", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void showCommentList(int actionType, List<Comment> commentList){
        if(actionType == STATE_REFRESH) mCommentList.clear();
        mCommentList.addAll(commentList);
        mPostDetailCommentAdapter.notifyDataSetChanged();
    }

    public void onClick(){
        mButtonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mLikesitem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PostDetailLikesActivity.actionStart(mContext, postObjectId);
            }
        });
        mTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Theme theme = new Theme();
                theme.setObjectId(themeObjectId);
                theme.setName(themeName);
                ThemePostListActivity.actionStart(mContext, theme);
            }
        });
        mPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchBigPhotoActivity.actionStart(mContext, photoUrl);
            }
        });
        mWriteComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WriteCommentActivity.actionStart(mContext, 0, postObjectId, null, null);
            }
        });
    }

    public class SendCommentReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent){
            getComment(STATE_REFRESH);
        }
    }
}
