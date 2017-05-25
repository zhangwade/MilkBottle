/*
 * Created by WadeZhang on 17-5-25 上午8:40
 * Copyright(c) 2017. All rights reserved.
 *
 * Last modified 17-5-24 下午3:49
 */

package com.wadezhang.milkbottle.register_and_login;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.wadezhang.milkbottle.BaseActivity;
import com.wadezhang.milkbottle.MainActivity;
import com.wadezhang.milkbottle.R;
import com.wadezhang.milkbottle.User;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;

/**
 * Created by Administrator on 2017/5/9 0009.
 */

public class WelcomeActivity extends BaseActivity {

    @BindView(R.id.activity_welcome_text_login) TextView mBtnLogin;
    @BindView(R.id.activity_welcome_text_register) TextView mBtnRegister;

    Context mContext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        SystemClock.sleep(2000);
        Bmob.initialize(this, "88948b7c6b14026453c55e34644c8b2c");
        User currentUser = BmobUser.getCurrentUser(User.class);
        if(currentUser != null){  //用户已登录，直接进入主界面
            MainActivity.actionStart(this);
            finish();
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        mContext = this;
        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterActivity.actionStart(mContext);
            }
        });
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginActivity.actionStart(mContext);
            }
        });
    }
}
