package com.wadezhang.milkbottle.register_and_login;

import android.os.Bundle;
import android.os.SystemClock;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.DayThemeSmallText);
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, "88948b7c6b14026453c55e34644c8b2c");
        User currentUser = BmobUser.getCurrentUser(User.class);
        if(currentUser != null){  //用户已登录，直接进入主界面
            MainActivity.actionStart(this);
            finish();
        }
        //SystemClock.sleep(2000);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
    }
}
