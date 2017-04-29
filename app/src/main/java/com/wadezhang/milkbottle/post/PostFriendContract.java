package com.wadezhang.milkbottle.post;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/28 0028.
 */

public interface PostFriendContract {

    interface View extends BaseView<Presenter>{
        void updateAdapter(List<Post> postList, int actionType);
        void showToast(String toast);
    }

    interface Presenter extends BasePresenter{
        void getPost(int actionType);
        void showPost(int actionType);
    }
}
