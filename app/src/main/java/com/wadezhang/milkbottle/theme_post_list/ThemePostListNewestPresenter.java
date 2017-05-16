package com.wadezhang.milkbottle.theme_post_list;

import android.util.Log;

import com.wadezhang.milkbottle.User;
import com.wadezhang.milkbottle.post.Post;
import com.wadezhang.milkbottle.theme.Theme;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public class ThemePostListNewestPresenter implements ThemePostListNewestContract.Presenter {

    private ThemePostListNewestContract.View mThemePostListNewestView;
    private List<Post> mPostList;
    //private boolean isFirstReq = true; //缓存策略的判断标志位。第一次进入应用的时候，设置其查询的缓存策略为CACHE_ELSE_NETWORK,当用户执行上拉或者下拉刷新操作时，设置查询的缓存策略为NETWORK_ELSE_CACHE。

    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 10; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多
    //private List<String> followIds = new ArrayList<String>();; //当前用户的关注

    private String mThemeId;

    public ThemePostListNewestPresenter(ThemePostListNewestContract.View view, String themeId){
        mThemePostListNewestView = view;
        mThemeId = themeId;
        mThemePostListNewestView.setPresenter(this);
    }

    @Override
    public void getPost(int actionType){
        /*
        User user = new User();
        user.setObjectId("C0NeXXX3");

        User user1 = new User();
        user1.setObjectId("36mF1118");
        BmobRelation relation1 = new BmobRelation();
        relation1.add(user1);
        user.setFollow(relation1);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.d(getClass().getSimpleName(), "bmob添加关注36mF1118成功：");
                }else{
                    Log.d(getClass().getSimpleName(), "bmob添加关注36mF1118失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        User user2 = new User();
        user2.setObjectId("C3a12227");
        BmobRelation relation2 = new BmobRelation();
        relation2.add(user2);
        user.setFollow(relation2);
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if(e == null){
                    Log.d(getClass().getSimpleName(), "bmob添加关注C3a12227成功：");
                }else{
                    Log.d(getClass().getSimpleName(), "bmob添加关注C3a12227失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });   */
        mActionType = actionType;
        Theme theme = new Theme();
        theme.setObjectId(mThemeId);
        BmobQuery<Post> query = new BmobQuery<>();
        // 按时间降序查询
        query.addWhereEqualTo("theme", theme);
        query.order("-createdAt");
        query.addQueryKeys("objectId,theme,author,photo,content,createdAt,commentCount,likesCount");
        query.include("author[objectId|icon|username]");
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
                        mThemePostListNewestView.showToast("到底了");
                    } else if (mActionType == STATE_REFRESH) {
                        mThemePostListNewestView.showToast("暂时没有帖子哦~~");
                    }
                }else{
                    mThemePostListNewestView.showToast("网络出了点小差~~");
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
/*
        followIds = new ArrayList<String>(); //TODO:测试用
        followIds.add("C3a12227"); //TODO:测试用
        followIds.add("36mF1118"); //TODO:测试用   */
/*
        if(followIds.isEmpty()){
            mPostFriendView.showToast("没有任何关注");
            return; //没有任何关注
        }   */
    }

    @Override
    public void showPost(int actionType){
        if(actionType == STATE_REFRESH) mThemePostListNewestView.updateAdapter(mPostList, 0);
        else mThemePostListNewestView.updateAdapter(mPostList, 1);
    }
}
