/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:10
 */

package com.wadezhang.milkbottle.post;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.wadezhang.milkbottle.GetCurrentUser;
import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.UserInfo;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PostFriendPresenter implements PostFriendContract.Presenter {

    private PostFriendContract.View mPostFriendView;
    private List<Post> mPostList;
    //private boolean isFirstReq = true; //缓存策略的判断标志位。第一次进入应用的时候，设置其查询的缓存策略为CACHE_ELSE_NETWORK,当用户执行上拉或者下拉刷新操作时，设置查询的缓存策略为NETWORK_ELSE_CACHE。

    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 10; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多
    private List<String> followIds = new ArrayList<String>();; //当前用户的关注

    Context mContext;

    public PostFriendPresenter(PostFriendContract.View view, Context context){
        mContext = context;
        mPostFriendView = view;
        mPostFriendView.setPresenter(this);
    }

    @Override
    public void getPost(int actionType){
        mActionType = actionType;
        final User me = GetCurrentUser.getCurrentUser(mContext);
        if(me == null)return;

        BmobQuery<UserInfo> query = new BmobQuery<>();
        User mUser = new User();
        mUser.setObjectId(me.getObjectId());
        query.addWhereEqualTo("user", mUser);
        query.addQueryKeys("objectId");
        query.findObjects(new FindListener<UserInfo>() {
            @Override
            public void done(List<UserInfo> list, BmobException e) {
                if(e == null && !list.isEmpty()){
                    BmobQuery<User> userQuery = new BmobQuery<User>();
                    userQuery.addWhereRelatedTo("follow", new BmobPointer(list.get(0)));
                    userQuery.addQueryKeys("objectId");
                    userQuery.findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) { //查询当前用户的关注
                            if(e == null){
                                followIds.clear();
                                if(!list.isEmpty()){
                                    for(User user : list){
                                        followIds.add(user.getObjectId());
                                    }
                                }
                                followIds.add(me.getObjectId()); //自己发的帖子也显示在“好友”栏里
                                getPostFromFollow();
                            }else{
                                Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                                Log.d(getClass().getSimpleName(), "bmob查询followIds失败："+e.getMessage()+","+e.getErrorCode());
                            }
                        }
                    });
                }else{
                    Toast.makeText(mContext, "请检查网络是否开启", Toast.LENGTH_SHORT).show();
                    Log.d(getClass().getSimpleName(), "bmob查询 UserInfo 失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void getPostFromFollow(){
        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereContainedIn("objectId", followIds);
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereMatchesQuery("author", "_User", innerQuery);
        // 按时间降序查询
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
                        mPostList = list;
                        // 这里在每次加载完数据后，将当前页码+1
                        curPage++;
                        showPost(mActionType);
                    } else if (mActionType == STATE_MORE) {
                        mPostFriendView.showToast("到底了");
                    } else if (mActionType == STATE_REFRESH) {
                        mPostFriendView.showToast("右滑发现更多精彩");
                    }
                }else{
                    mPostFriendView.showToast("网络出了点小差~~");
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    @Override
    public void showPost(int actionType){
        if(actionType == STATE_REFRESH) mPostFriendView.updateAdapter(mPostList, 0);
            else mPostFriendView.updateAdapter(mPostList, 1);
    }
}
