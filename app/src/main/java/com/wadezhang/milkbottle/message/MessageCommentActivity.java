package com.wadezhang.milkbottle.message;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.post_detail.Comment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class MessageCommentActivity extends BaseActivity {

    @BindView(R.id.activity_message_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_message_text_title)TextView mTitle;
    @BindView(R.id.activity_message_swipetoloadlayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRecyclerView;

    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 30; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多

    private List<Comment> mCommentList = new ArrayList<>();
    private MessageCommentAdapter mMessageCommentAdapter;

    private List<Post> myPostList = new ArrayList<>();; //我的所有帖子的 Id
    User mUser;

    Context mContext;

    public static void actionStart(Context context){
        Intent intent = new Intent(context, MessageCommentActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        mContext = this;
        mTitle.setText("评论");
        mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageCommentAdapter = new MessageCommentAdapter(mCommentList);
        mRecyclerView.setAdapter(mMessageCommentAdapter);
        autoRefresh();
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void autoRefresh(){
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    public class RefreshListener implements OnRefreshListener {

        @Override
        public void onRefresh(){
            getMyPostIds(STATE_REFRESH);
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener {

        @Override
        public void onLoadMore(){
            getMyPostIds(STATE_MORE);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    public void getMyPostIds(int actionType){
        mActionType = actionType;
        mUser = GetCurrentUser.getCurrentUser(mContext);
        if(mUser == null) return;
        BmobQuery<Post> mMyPostQuery = new BmobQuery<>();
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
                    getComment();
                }else{
                    Toast.makeText(mContext, "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询myPostIds失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void getComment(){
        BmobQuery<Comment> q1 = new BmobQuery<>();
        q1.addWhereContainedIn("post", myPostList); //TODO: myPostList 可能为空导致有问题
        BmobQuery<Comment> q2 = new BmobQuery<>();
        q2.addWhereEqualTo("toWho", mUser);
        List<BmobQuery<Comment>> queries = new ArrayList<BmobQuery<Comment>>();
        queries.add(q1);
        queries.add(q2);
        BmobQuery<Comment> commentBmobQuery = new BmobQuery<>();
        commentBmobQuery.or(queries);
        commentBmobQuery.order("-createdAt");
        commentBmobQuery.addQueryKeys("objectId,author,content,createdAt,post");
        commentBmobQuery.include("author[objectId|icon|nickname],post[objectId|photo]");
        // 如果是加载更多
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
            commentBmobQuery.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            commentBmobQuery.setSkip(curPage * limit - 1);
        }
        // 设置每页数据个数
        commentBmobQuery.setLimit(limit);
        // 查找数据
        commentBmobQuery.findObjects(new FindListener<Comment>() {
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
                        showUserList(mActionType, list);
                    } else if (mActionType == STATE_MORE) {
                        Toast.makeText(mContext, "到底了", Toast.LENGTH_SHORT).show();
                    } else if (mActionType == STATE_REFRESH) {
                        Toast.makeText(mContext, "还没有评论消息", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void showUserList(int actionType, List<Comment> postList){
        if(actionType == STATE_REFRESH) mCommentList.clear();
        mCommentList.addAll(postList);
        mMessageCommentAdapter.notifyDataSetChanged();
    }
}
