package com.wadezhang.milkbottle.me;

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
import com.wadezhang.milkbottle.UserInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/12 0012.
 */

public class FollowOrFansListActivity extends BaseActivity {

    @BindView(R.id.activity_follow_or_fans_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_follow_or_fans_text_title) TextView mTitle;
    @BindView(R.id.activity_follow_or_fans_imgbtn_search) ImageButton mBtnSearch;
    @BindView(R.id.activity_follow_or_fans_swipetoloadlayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRecyclerView;

    //private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private String lastNickname = "WadeZhang";
    private int limit = 30; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多

    private int mType;
    private final int TYPE_FOLLOW = 0;
    private final int TYPE_FANS = 1;

    private String mUserInfoId;

    private List<User> mUserList = new ArrayList<>();
    private FollowOrFansAdapter mFollowOrFansAdapter;

    Context mContext;

    public static void actionStart(Context context, int type, String userInfoId){
        Intent intent = new Intent(context, FollowOrFansListActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("userInfoId", userInfoId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_or_fans);
        ButterKnife.bind(this);
        mContext = this;
        Intent mIntent = getIntent();
        mType = mIntent.getIntExtra("type", TYPE_FOLLOW);
        mUserInfoId = mIntent.getStringExtra("userInfoId");
        if(mType == TYPE_FANS) mTitle.setText("粉丝");
        mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mFollowOrFansAdapter = new FollowOrFansAdapter(mUserList);
        mRecyclerView.setAdapter(mFollowOrFansAdapter);
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
            getUserList(STATE_REFRESH);
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener {

        @Override
        public void onLoadMore(){
            getUserList(STATE_MORE);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    public void getUserList(int actionType){
        mActionType = actionType;
        User user = GetCurrentUser.getCurrentUser(mContext);
        if(user == null) return;
        BmobQuery<User> query = new BmobQuery<>();
        UserInfo userInfo = new UserInfo();
        userInfo.setObjectId(mUserInfoId);
        if(mType == TYPE_FOLLOW) query.addWhereRelatedTo("follow", new BmobPointer(userInfo));
            else  query.addWhereRelatedTo("fans", new BmobPointer(userInfo));
        query.order("nickname");
        query.addQueryKeys("objectId,icon,nickname");
        // 如果是加载更多
        if (mActionType == STATE_MORE) {
            /* // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                //Log.i("0414", date.toString());
            } catch (ParseException e1) {
                e1.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据  */
            query.addWhereLessThanOrEqualTo("nickname", new BmobObject(lastNickname));
            // 跳过之前页数并去掉重复数据
            query.setSkip(curPage * limit - 1);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if(e == null){
                    if (!list.isEmpty()) {
                        if (mActionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            // 获取目前最新的时间
                            lastNickname = list.get(0).getNickname();
                        }
                        // 这里在每次加载完数据后，将当前页码+1
                        curPage++;
                        showUserList(mActionType, list);
                    } else if (mActionType == STATE_MORE) {
                        Toast.makeText(mContext, "到底了", Toast.LENGTH_SHORT).show();
                    } else if (mActionType == STATE_REFRESH) {
                        if(mType == TYPE_FOLLOW) Toast.makeText(mContext, "还没关注任何人", Toast.LENGTH_SHORT).show();
                        else Toast.makeText(mContext, "还没有粉丝哦~~", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void showUserList(int actionType, List<User> userList){
        if(actionType == STATE_REFRESH) mUserList.clear();
        mUserList.addAll(userList);
        mFollowOrFansAdapter.notifyDataSetChanged();
    }
}
