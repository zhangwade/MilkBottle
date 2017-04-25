package com.wadezhang.milkbottle.book;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public class BookShelfContract {

    interface View extends BaseView<BookShopContract.Presenter> {
        void updateAdapter(List<Book> bookList);
    }

    interface Presenter extends BasePresenter {
        void showBook();
    }
}
