package com.wadezhang.milkbottle.new_post;

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
import com.wadezhang.milkbottle.theme.Theme;
import com.wadezhang.milkbottle.theme_category.ThemeCategory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/15 0015.
 */

public class SelectThemeActivity extends BaseActivity {

    @BindView(R.id.activity_theme_list_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_theme_list_text_theme_category) TextView mTitle;
    @BindView(R.id.activity_theme_list_text_my_follow_theme) TextView mMyFollowTheme;
    @BindView(R.id.activity_theme_list_swipetoloadlayout) SwipeToLoadLayout mSwipeToLoadLayout;
    @BindView(R.id.swipe_target) RecyclerView mRecyclerView;

    Context mContext;

    private String mThemeCategoryId;
    private String mThemeCategoryTitle;

    private SelectThemeAdapter mThemeListAdapter;
    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 20; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType; //下拉刷新 还是 上拉加载更多
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多
    private List<Theme> mThemeList = new ArrayList<>();

    public static void actionStart(Context context, ThemeCategory themeCategory){
        Intent intent = new Intent(context, SelectThemeActivity.class);
        intent.putExtra("themeCategoryId", themeCategory.getObjectId());
        intent.putExtra("themeCategoryTitle", themeCategory.getTitle());
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_theme_list);
        ButterKnife.bind(this);
        init();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mThemeListAdapter = new SelectThemeAdapter(mThemeList);
        mRecyclerView.setAdapter(mThemeListAdapter);
        autoRefresh();
        mSwipeToLoadLayout.setOnRefreshListener(new RefreshListener());
        mSwipeToLoadLayout.setOnLoadMoreListener(new LoadMoreListener());
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void init(){
        Intent mIntent = getIntent();
        mThemeCategoryId = mIntent.getStringExtra("themeCategoryId");
        mThemeCategoryTitle = mIntent.getStringExtra("themeCategoryTitle");
        mTitle.setText(mThemeCategoryTitle);
        mMyFollowTheme.setVisibility(View.INVISIBLE);
    }

    public void autoRefresh() {
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
            getThemeList(STATE_REFRESH);
            mSwipeToLoadLayout.setRefreshing(false);
        }
    }

    public class LoadMoreListener implements OnLoadMoreListener {

        @Override
        public void onLoadMore(){
            getThemeList(STATE_MORE);
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    public void getThemeList(int actionType){
        mActionType = actionType;
        BmobQuery<Theme> query = new BmobQuery<>();
        ThemeCategory themeCategory = new ThemeCategory();
        themeCategory.setObjectId(mThemeCategoryId);
        query.addWhereEqualTo("category", themeCategory);
        query.order("-createdAt");
        query.addQueryKeys("objectId,name,postCount");
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
            query.addWhereLessThanOrEqualTo("updatedAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(curPage * limit - 1);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<Theme>() {
            @Override
            public void done(List<Theme> list, BmobException e) {
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
                        updateThemeList(mActionType, list);
                    } else if (mActionType == STATE_MORE) {
                        Toast.makeText(mContext, "到底了", Toast.LENGTH_SHORT).show();
                    } else if (mActionType == STATE_REFRESH) {
                        Toast.makeText(mContext, "暂时没有话题哦~~", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询话题失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void updateThemeList(int actionType, List<Theme> list){
        if(actionType == STATE_REFRESH) mThemeList.clear();
        mThemeList.addAll(list);
        mThemeListAdapter.notifyDataSetChanged();
    }
}
