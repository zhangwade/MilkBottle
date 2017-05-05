package com.wadezhang.milkbottle.theme;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public interface ThemeContract {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
    }
}
