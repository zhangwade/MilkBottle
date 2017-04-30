package com.wadezhang.milkbottle.post;

import android.util.Log;
import android.widget.Toast;

import com.wadezhang.milkbottle.User;

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
 * Created by Administrator on 2017/4/28 0028.
 */

public class PostFriendPresenter implements PostFriendContract.Presenter {

    private PostFriendContract.View mPostFriendView;
    private List<Post> mPostList;
    //private boolean isFirstReq = true; //缓存策略的判断标志位。第一次进入应用的时候，设置其查询的缓存策略为CACHE_ELSE_NETWORK,当用户执行上拉或者下拉刷新操作时，设置查询的缓存策略为NETWORK_ELSE_CACHE。

    private String lastTime; //查询数据的时间边界
    private int limit = 10; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多
    private List<String> followIds; //当前用户的关注

    public PostFriendPresenter(PostFriendContract.View view){
        mPostFriendView = view;
        mPostFriendView.setPresenter(this);
    }

    @Override
    public void getPost(final int actionType){
        /*
        BmobQuery<User> mFollowQuery = new BmobQuery<>();
        User mUser = new User();
        mUser.setObjectId("C0NeXXX3"); //TODO:做好注册登录后要更改这里
        mFollowQuery.addWhereRelatedTo("follow", new BmobPointer(mUser));
        mFollowQuery.addQueryKeys("objectId");
        mFollowQuery.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) { //查询当前用户的关注
                if(e == null){
                    followIds = new ArrayList<String>();
                    for(User user : list) followIds.add(user.getObjectId());
                }else{
                    Log.d(getClass().getSimpleName(), "bmob查询followIds失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });    */

        followIds = new ArrayList<String>(); //TODO:测试用
        followIds.add("C3a12227"); //TODO:测试用
        followIds.add("36mF1118"); //TODO:测试用

        if(followIds.size() < 1) return; //没有任何关注
        BmobQuery<User> innerQuery = new BmobQuery<>();
        innerQuery.addWhereContainedIn("objectId", followIds);
        BmobQuery<Post> query = new BmobQuery<>();
        query.addWhereMatchesQuery("author", "_User", innerQuery);
        // 按时间降序查询
        query.order("-createdAt");
        query.addQueryKeys("objectId,theme,author,photo,content,createdAt,commentCount,likesCount");
        query.include("theme[name],author[icon|username]");
        // 如果是加载更多
        if (actionType == STATE_MORE) {
            // 处理时间查询
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                date = sdf.parse(lastTime);
                //Log.i("0414", date.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // 只查询小于等于最后一个item发表时间的数据
            query.addWhereLessThanOrEqualTo("createdAt", new BmobDate(date));
            // 跳过之前页数并去掉重复数据
            query.setSkip(curPage * limit + 1);
        }
        // 设置每页数据个数
        query.setLimit(limit);
        // 查找数据
        query.findObjects(new FindListener<Post>() {
            @Override
            public void done(List<Post> list, BmobException e) {
                if(e == null){
                    if (list.size() > 0) {
                        if (actionType == STATE_REFRESH) {
                            // 当是下拉刷新操作时，将当前页的编号重置为0，并把bankCards清空，重新添加
                            curPage = 0;
                            // 获取目前最新的时间
                            lastTime = list.get(0).getCreatedAt();
                        }
                        mPostList = list;
                        // 这里在每次加载完数据后，将当前页码+1
                        curPage++;
                        showPost(actionType);
                    } else if (actionType == STATE_MORE) {
                        mPostFriendView.showToast("到底了");
                    } else if (actionType == STATE_REFRESH) {
                        mPostFriendView.showToast("没有数据");
                    }
                }else{
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
        //isFirstReq = false;
    }

    @Override
    public void showPost(int actionType){
        if(actionType == STATE_REFRESH) mPostFriendView.updateAdapter(mPostList, 0);
        mPostFriendView.updateAdapter(mPostList, 1);
    }
}
