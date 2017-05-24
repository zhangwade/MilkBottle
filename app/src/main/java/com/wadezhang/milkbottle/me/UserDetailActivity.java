package com.wadezhang.milkbottle.me;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import com.wadezhang.milkbottle.UserInfo;
import com.wadezhang.milkbottle.message.Fans;
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
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

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

    private int isFollow = 0; //对用户是否已关注，0 代表未检查完，1 代表已关注，2 代表未关注
    private String fansRecordId; //新粉丝通知表的记录 ID
    private Handler handler;

    Context mContext;

    private String mUserId;
    private String mIconUrl;
    private int themeCount = 0;

    public static void actionStart(Context context, String userId){
        Intent intent = new Intent(context, UserDetailActivity.class);
        intent.putExtra("userId", userId);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
        ButterKnife.bind(this);
        mContext = this;
        init();
        isFollow = 0;
        mAddFollow.setEnabled(false);
        checkIsFollow();
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mPostRecyclerView.setLayoutManager(mLinearLayoutManager);
        mPostAdapter = new PostFriendAdapter(mPostList, 0);
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
        query.addQueryKeys("icon,nickname,sex,introduction");
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

        themeCount = 0;
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

    public void checkIsFollow(){
        User me = GetCurrentUser.getCurrentUser(mContext);
        if(me == null) return;
        if(me.getObjectId().equals(mUserId)){
            mAddFollow.setVisibility(View.GONE);
            return;
        }

        BmobQuery<Fans> query = new BmobQuery<>();
        User from = new User();
        from.setObjectId(me.getObjectId());
        query.addWhereEqualTo("from", me);
        User to = new User();
        to.setObjectId(mUserId);
        query.addWhereEqualTo("to", to);
        query.findObjects(new FindListener<Fans>() {
            @Override
            public void done(List<Fans> list, BmobException e) {
                if(e == null){
                    mAddFollow.setEnabled(true);
                    if(!list.isEmpty()){
                        mAddFollow.setText(" 已关注 ");
                        isFollow = 1;
                        fansRecordId = list.get(0).getObjectId();
                        //if(tag == 0) mAddFollow.setText(" 已关注 ");
                            //else cancelFollow(list.get(0).getObjectId());
                    }else{
                        isFollow = 2;
                        //if(tag == 1) addFollow();
                    }
                }else {
                    Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询是否已关注失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void addFollow(){
        final User me = GetCurrentUser.getCurrentUser(mContext); //我
        if(me == null) return;
        //mAddFollow.setText(" 已关注 ");

        Fans fans = new Fans(); //添加关注记录
        User from = new User();
        from.setObjectId(me.getObjectId());
        fans.setFrom(from);
        User to = new User();  //被浏览的用户
        to.setObjectId(mUserId);
        fans.setTo(to);
        fans.setIsRead(0);
        fans.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if(e == null){
                    fansRecordId = s;
                    addFollowSec();
                }else{
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.d(getClass().getSimpleName(), "bmob添加新粉丝通知记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void addFollowSec(){
        final User me = GetCurrentUser.getCurrentUser(mContext); //我
        if(me == null) return;

        User u1 = new User();  //添加到我的关注列表
        u1.setObjectId(me.getObjectId());
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", u1);
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    UserInfo u1 = new UserInfo();
                    u1.setObjectId(list.get(0).getObjectId());
                    BmobRelation bmobRelation = new BmobRelation();
                    User u2 = new User();
                    u2.setObjectId(mUserId);
                    bmobRelation.add(u2);
                    u1.setFollow(bmobRelation);
                    u1.increment("followCount");
                    u1.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                addFollowThird();
                            }else{
                                Message msg = new Message();
                                msg.what = 0;
                                handler.sendMessage(msg);
                                Log.d(getClass().getSimpleName(), "bmob添加关注失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else{
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.d(getClass().getSimpleName(), "bmob查询 UserInfo Id 失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void addFollowThird(){
        final User me = GetCurrentUser.getCurrentUser(mContext); //我
        if(me == null) return;

        User user1 = new User();  //将我添加到用户的粉丝列表
        user1.setObjectId(mUserId);
        BmobQuery<UserInfo> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", user1);
        query1.addQueryKeys("objectId");
        query1.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    UserInfo user1 = new UserInfo();
                    user1.setObjectId(list.get(0).getObjectId());
                    BmobRelation relation = new BmobRelation();
                    User user2 = new User();
                    user2.setObjectId(me.getObjectId());
                    relation.add(user2);
                    user1.setFans(relation);
                    user1.increment("fansCount");
                    user1.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Message msg = new Message();
                            if(e == null){
                                msg.what = 1;
                            }else{
                                msg.what = 0;
                                Log.d(getClass().getSimpleName(), "bmob添加粉丝失败："+e.getMessage()+","+e.getErrorCode());
                            }
                            handler.sendMessage(msg);
                        }
                    });
                }else{
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.d(getClass().getSimpleName(), "bmob查询 UserInfo Id 失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void cancelFollow(){
        final User me = GetCurrentUser.getCurrentUser(mContext);
        if(me == null) return;
        //mAddFollow.setText(" + 关注 ");

        Fans fans = new Fans();
        fans.setObjectId(fansRecordId);
        fans.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    cancelFollowSec();
                }else{
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.d(getClass().getSimpleName(), "bmob删除新粉丝通知记录失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void cancelFollowSec(){
        final User me = GetCurrentUser.getCurrentUser(mContext);
        if(me == null) return;

        User u1 = new User();  //取消我对用户的关注
        u1.setObjectId(me.getObjectId());
        BmobQuery<UserInfo> query = new BmobQuery<>();
        query.addWhereEqualTo("user", u1);
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    UserInfo u1 = new UserInfo();
                    u1.setObjectId(list.get(0).getObjectId());
                    User u2 = new User();
                    u2.setObjectId(mUserId);
                    BmobRelation relation = new BmobRelation();
                    relation.remove(u2);
                    u1.setFollow(relation);
                    u1.increment("followCount",-1);
                    u1.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e == null){
                                cancelFollowThird();
                            }else{
                                Message msg = new Message();
                                msg.what = 0;
                                handler.sendMessage(msg);
                                Log.d(getClass().getSimpleName(), "bmob取消关注失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else{
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.d(getClass().getSimpleName(), "bmob查询 UserInfo Id 失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void cancelFollowThird(){
        final User me = GetCurrentUser.getCurrentUser(mContext);
        if(me == null) return;

        User user1 = new User();  //把我撤出用户的粉丝列表
        user1.setObjectId(mUserId);
        BmobQuery<UserInfo> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", user1);
        query1.addQueryKeys("objectId");
        query1.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    UserInfo user1 = new UserInfo();
                    user1.setObjectId(list.get(0).getObjectId());
                    User user2 = new User();
                    user2.setObjectId(me.getObjectId());
                    BmobRelation bmobRelation = new BmobRelation();
                    bmobRelation.remove(user2);
                    user1.setFans(bmobRelation);
                    user1.increment("fansCount",-1);
                    user1.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            Message msg = new Message();
                            if(e == null){
                                msg.what = 1;
                            }else{
                                msg.what = 0;
                                Log.d(getClass().getSimpleName(), "bmob取消粉丝失败："+e.getMessage()+","+e.getErrorCode());
                            }
                            handler.sendMessage(msg);
                        }
                    });
                }else {
                    Message msg = new Message();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.d(getClass().getSimpleName(), "bmob查询 UserInfo Id 失败："+e.getMessage()+","+e.getErrorCode());
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
        query.include("theme[objectId|name],author[objectId|icon|nickname]");
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
        mAddFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mProgressDialog = new ProgressDialog(mContext);
                mProgressDialog.setMessage("正在操作...");
                mProgressDialog.setCancelable(false);
                if(isFollow == 1){
                    AlertDialog.Builder mAlertDialog = new AlertDialog.Builder(mContext);
                    mAlertDialog.setMessage("确定取消关注?");
                    mAlertDialog.setCancelable(true);
                    mAlertDialog.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mProgressDialog.show();
                            cancelFollow();
                        }
                    });
                    mAlertDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    mAlertDialog.show();
                }else if(isFollow == 2){
                    mProgressDialog.show();
                    addFollow();
                }
                handler = new Handler(){
                    public void handleMessage(Message msg){
                        mProgressDialog.dismiss();
                        switch (msg.what){
                            case 1 :
                                if(isFollow == 1){
                                    mAddFollow.setText(" + 关注 ");
                                    isFollow = 2;
                                }else if (isFollow == 2){
                                    mAddFollow.setText(" 已关注 ");
                                    isFollow = 1;
                                }
                                break;
                            case 0 :
                                Toast.makeText(mContext, "操作失败! 请检查网络是否开启", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                break;
                        }
                    }
                };
            }
        });
    }
}
