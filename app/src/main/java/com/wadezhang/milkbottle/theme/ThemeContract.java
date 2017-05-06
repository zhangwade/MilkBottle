package com.wadezhang.milkbottle.theme;

import com.wadezhang.milkbottle.BasePresenter;
import com.wadezhang.milkbottle.BaseView;

import java.util.List;

/**
 * Created by Administrator on 2017/5/5 0005.
 */

public interface ThemeContract {

    interface View extends BaseView<Presenter> {
        void updateAdapter(List<Theme> themeList, int secondGroupPosition);
        void showToast(String toast);
    }

    interface Presenter extends BasePresenter {
        void start(int hotItem, int newestItem);
        void showTheme();
    }
}
