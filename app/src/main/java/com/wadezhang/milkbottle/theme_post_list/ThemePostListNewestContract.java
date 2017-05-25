/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-7 下午9:38
 */

package com.wadezhang.milkbottle.theme_post_list;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;
import com.wadezhang.milkbottle.post.Post;

import java.util.List;

/**
 * Created by Administrator on 2017/5/7 0007.
 */

public interface ThemePostListNewestContract {

    interface View extends BaseView<Presenter> {
        void updateAdapter(List<Post> postList, int actionType);
        void showToast(String toast);
    }

    interface Presenter extends BasePresenter {
        void getPost(int actionType);
        void showPost(int actionType);
    }
}
