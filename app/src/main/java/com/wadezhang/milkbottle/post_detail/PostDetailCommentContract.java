/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-2 下午5:31
 */

package com.wadezhang.milkbottle.post_detail;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/2 0002.
 */

public interface PostDetailCommentContract {

    interface View extends BaseView<Presenter> {
        void updateAdapter(List<Comment> postList, int actionType);
        void showToast(String toast);
    }

    interface Presenter extends BasePresenter {
        void getComment(int actionType);
        void showComment(int actionType);
    }
}
