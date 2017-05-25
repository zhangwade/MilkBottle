/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-29 下午1:28
 */

package com.wadezhang.milkbottle.book;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25 0025.
 */

public interface BookShelfContract {

    interface View extends BaseView<Presenter> {
        void updateAdapter(List<Book> bookList);
    }

    interface Presenter extends BasePresenter {
        void start();
        void showBook();
    }
}
