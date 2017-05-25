/*
 * Created by WadeZhang on 17-5-25 上午8:39
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-4-29 下午1:28
 */

package com.wadezhang.milkbottle.book;

import android.os.Handler;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

/**
 * Created by Administrator on 2017/4/26 0026.
 */

public interface BookContentContract {

    interface View extends BaseView<Presenter>{
        void updateBookContent(String content);
    }

    interface Presenter extends BasePresenter{
        void start();
        void setPageSize(int pageSize, Handler handler);
        void getPreviousPage();
        void getNextPage();
        void showBookContent();
    }
}
