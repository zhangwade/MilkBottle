package com.wadezhang.milkbottle.me;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.ImageLoader;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.post.PostFriendAdapter;
import com.wadezhang.milkbottle.theme.Theme;
import com.wadezhang.milkbottle.theme_category.ThemeListActivity;
import com.wadezhang.milkbottle.watch_big_photo.WatchBigPhotoActivity;

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
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by Administrator on 2017/5/16 0016.
 */

public class UserDetailActivity extends BaseActivity {

    @BindView(R.id.activity_user_detail_imgbtn_back) ImageButton mBtnBack;
    @BindView(R.id.activity_user_detail_text_add_follow) TextView mAddFollow;
    @BindView(R.id.activity_user_detail_img_icon) ImageView mIcon;
    @BindView(R.id.activity_user_detail_text_nickname) TextView mNickname;
    @BindView(R.id.activity_user_detail_text_sex) TextView mSex;
    @BindView(R.id.activity_user_detail_text_introduction) TextView mIntroduction;
    @BindView(R.id.activity_user_detail_post_item) LinearLayout mPostItem;
    @BindView(R.id.activity_user_detail_text_post_count) TextView mPostCount;
    @BindView(R.id.activity_user_detail_theme_item) LinearLayout mThemeItem;
    @BindView(R.id.activity_user_detail_text_theme_count) TextView mThemeCount;
    @BindView(R.id.activity_user_detail_post_recyclerview) RecyclerView mPostRecyclerView;

    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 10; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多

    private List<Post> mPostList = new ArrayList<>();
    private PostFriendAdapter mPostAdapter;

    Context mContext;

    private String mUserId;
    private String mIconUrl;
    private int themeCount = 0;

    public static void actionStart(Context context, String userId){
        Intent intent = new Intent(context, MyPostActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        mContext = this;
        init();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mPostRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostAdapter = new PostFriendAdapter(mPostList);
        mPostRecyclerView.setAdapter(mPostAdapter);
        mPostRecyclerView.addOnScrollListener(new LoadMoreListener());
        getPost(STATE_REFRESH);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        onClick();
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
                getPost(STATE_MORE);
            }
        }
    }

    protected boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }

    public void init(){
        Intent mIntent = getIntent();
        mUserId = mIntent.getStringExtra("userId");
        BmobQuery<User> query = new BmobQuery<>();
        query.addQueryKeys("icon,nickname,sex,introduction,followCount,fansCount");
        query.getObject(mUserId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if(e == null){
                    mIconUrl = user.getIcon().getFileUrl();
                    ImageLoader.with(mContext, mIconUrl, mIcon);
                    mNickname.setText(user.getNickname());
                    mSex.setText(user.getSex());
                    mIntroduction.setText(user.getIntroduction());
                }else{
                    Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        final User user = new User();
        user.setObjectId(mUserId);
        BmobQuery<Post> postQuery = new BmobQuery<>();
        postQuery.addWhereEqualTo("author", user);
        postQuery.count(Post.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    mPostCount.setText(integer.toString());
                }else{
                    Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        BmobQuery<Theme> themeQuery = new BmobQuery<>();
        themeQuery.addWhereEqualTo("author", user);
        themeQuery.count(Theme.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if(e == null){
                    themeCount += integer;
                    BmobQuery<Theme> themeBmobQuery = new BmobQuery<>();
                    themeBmobQuery.addWhereRelatedTo("theme", new BmobPointer(user));
                    themeBmobQuery.addQueryKeys("objectId");
                    themeBmobQuery.findObjects(new FindListener<Theme>() {
                        @Override
                        public void done(List<Theme> list, BmobException e) {
                            if(e == null){
                                if(!list.isEmpty()){
                                    themeCount += list.size();
                                }
                                mThemeCount.setText(Integer.toString(themeCount));
                            }else{
                                Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else {
                    Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void getPost(int actionType){
        mActionType = actionType;
        BmobQuery<Post> query = new BmobQuery<>();
        User mUser = new User();
        mUser.setObjectId(mUserId);
        query.addWhereEqualTo("author", mUser);
        query.order("-createdAt");
        query.addQueryKeys("objectId,theme,author,photo,content,createdAt,commentCount,likesCount");
        query.include("theme[objectId|name],author[objectId|icon|username]");
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
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(curPage * limit - 1);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
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
                        Toast.makeText(mContext, "还没有发布任何帖子", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(mContext, "网络出了点小差~~", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void showUserList(int actionType, List<Post> postList){
        if(actionType == STATE_REFRESH) mPostList.clear();
        mPostList.addAll(postList);
        mPostAdapter.notifyDataSetChanged();
    }

    public void onClick(){
        mIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WatchBigPhotoActivity.actionStart(mContext, mIconUrl);
            }
        });
        mThemeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemeListActivity.actionStart(mContext, null, 3, mUserId);
            }
        });
    }
}
