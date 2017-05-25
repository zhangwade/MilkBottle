/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-1 下午1:49
 */

package com.wadezhang.milkbottle.post;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/1 0001.
 */

public interface PostFindContract {

    interface View extends BaseView<Presenter> {
        void updateAdapter(List<Post> postList, int actionType);
        void showToast(String toast);
    }

    interface Presenter extends BasePresenter{
        void getPost(int actionType);
        void showPost(int actionType);
    }
}
