package com.wadezhang.milkbottle.post_detail;

import android.os.Bundle;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/4/11 0011.
 */

public class PostDetailActivity extends BaseActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText); //TODO: 判断再切换主题
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);
        ButterKnife.bind(this);
    }
}
