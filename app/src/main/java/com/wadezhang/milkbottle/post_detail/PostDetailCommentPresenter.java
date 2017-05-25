/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:44
 */

package com.wadezhang.milkbottle.post_detail;

import android.util.Log;

import com.wadezhang.milkbottle.post.Post;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public class PostDetailCommentPresenter implements PostDetailCommentContract.Presenter{

    private PostDetailCommentContract.View mPostDetailCommentView;
    private List<Comment> mCommentList;
    private String mPostObjectId;

    private String lastTime = "2017-05-03 10:41:00"; //查询数据的时间边界
    private int limit = 10; //每次查询限制数目
    private int curPage = 0; //分页查询，当前所在页
    private int mActionType;
    private final int STATE_REFRESH = 0; //下拉刷新
    private final int STATE_MORE = 1; //上拉加载更多

    public PostDetailCommentPresenter(String postObjectId, PostDetailCommentContract.View view){
        mPostObjectId = postObjectId;
        mPostDetailCommentView = view;
        mPostDetailCommentView.setPresenter(this);
    }

    public void getComment(int actionType){
        mActionType = actionType;
        BmobQuery<Comment> mCommentQuery = new BmobQuery<>();
        Post mPost = new Post();
        mPost.setObjectId(mPostObjectId);
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
                        mCommentList = list;
                        // 这里在每次加载完数据后，将当前页码+1
                        curPage++;
                        showComment(mActionType);
                    } else if (mActionType == STATE_MORE) {
                        mPostDetailCommentView.showToast("到底了");
                    } else if (mActionType == STATE_REFRESH) {
                        mPostDetailCommentView.showToast("来发表第一条评论吧~~");
                    }
                }else{
                    Log.d(getClass().getSimpleName(), "bmob查询失败："+e.getMessage()+","+e.getErrorCode());
                }
            }
        });
    }

    public void showComment(int actionType){
        if(actionType == STATE_REFRESH) mPostDetailCommentView.updateAdapter(mCommentList, 0);
        else mPostDetailCommentView.updateAdapter(mCommentList, 1);
    }
}
