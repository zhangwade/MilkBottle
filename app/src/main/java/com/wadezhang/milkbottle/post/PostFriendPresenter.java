package com.wadezhang.milkbottle.post;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public class PostFriendPresenter implements PostFriendContract.Presenter {

    private PostFriendContract.View mPostFriendView;
    private List<Post> mPostList;
    private boolean isFirstReq = true; //缓存策略的判断标志位。第一次进入应用的时候，设置其查询的缓存策略为CACHE_ELSE_NETWORK,当用户执行上拉或者下拉刷新操作时，设置查询的缓存策略为NETWORK_ELSE_CACHE。

    public PostFriendPresenter(PostFriendContract.View view){
        mPostFriendView = view;
        mPostFriendView.setPresenter(this);
    }

    @Override
    public void start(){

    }
}
