package com.wadezhang.milkbottle.book;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public interface BookShopContract {

    public interface View extends BaseView<Presenter> {
        void showLoading();
        void hideLoading();
        void showTitle(String title);
        void showContent(String content);
    }

    public interface Presenter extends BasePresenter {
        void showBook();
    }
}
