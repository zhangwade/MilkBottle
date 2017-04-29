package com.wadezhang.milkbottle.book;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/24 0024.
 */

public interface BookShopContract {

    interface View extends BaseView<Presenter> {
        void updateAdapter(List<Book> bookList);
    }

    interface Presenter extends BasePresenter {
        void start();
        void showBook();
    }
}
