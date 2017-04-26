package com.wadezhang.milkbottle.book;

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
        void getPreviousPage();
        void getNextPage();
        void showBookContent();
    }
}
